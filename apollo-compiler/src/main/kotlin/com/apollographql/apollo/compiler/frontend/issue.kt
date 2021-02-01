package com.apollographql.apollo.compiler.frontend

data class ParseResult<out T>(
    val value: T,
    val issues: List<Issue>
) {
  fun orThrow(): T {
    val firstError = issues.firstOrNull { it.severity == Issue.Severity.ERROR }
    if (firstError != null) {
      throw SourceAwareException(firstError.message, firstError.sourceLocation)
    }
    return value
  }

  fun <R> mapValue(transform: (T) -> R): ParseResult<R> {
    return ParseResult(
        transform(value),
        issues
    )
  }

  fun <R> flatMap(transform: (T) -> ParseResult<R>): ParseResult<R> {
    val transformedResult = transform(value)
    return ParseResult(
        transformedResult.value,
        issues + transformedResult.issues
    )
  }


  fun appendIssues(newIssues: (T) -> List<Issue>): ParseResult<T> {
    return ParseResult(
        value,
        issues + newIssues(value)
    )
  }
}

/**
 * All the issues that can be collected while analyzing a graphql document
 */
sealed class Issue(
    val message: String,
    val sourceLocation: SourceLocation,
    val severity: Severity
) {
  class ValidationError(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.ERROR)
  class UnkownError(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.ERROR)
  class ParsingError(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.ERROR)
  class DeprecatedUsage(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.WARNING)
  /**
   * In a perfect world everyone uses SDL schemas and we can validate directives but in this world, a lot of users rely
   * on introspection schemas that do not contain directives. If this happens, we pass them through without validation.
   */
  class UnknownDirective(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.WARNING)
  class UnusedVariable(message: String, sourceLocation: SourceLocation) : Issue(message, sourceLocation, Severity.WARNING)

  enum class Severity {
    WARNING,
    ERROR
  }
}
