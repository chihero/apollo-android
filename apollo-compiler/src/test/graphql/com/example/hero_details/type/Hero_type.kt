// AUTO-GENERATED FILE. DO NOT MODIFY.
//
// This class was automatically generated by Apollo GraphQL plugin from the GraphQL queries it found.
// It should not be modified by hand.
//
package com.example.hero_details.type

import com.apollographql.apollo.api.EnumValue
import kotlin.String

/**
 * Lower case enum type name
 */
enum class Hero_type(
  override val rawValue: String
) : EnumValue {
  HUMAN("human"),

  DROID("droid"),

  /**
   * Auto generated constant for unknown enum values
   */
  UNKNOWN__("UNKNOWN__");

  companion object {
    fun safeValueOf(rawValue: String): Hero_type = values().find { it.rawValue == rawValue } ?:
        UNKNOWN__
  }
}
