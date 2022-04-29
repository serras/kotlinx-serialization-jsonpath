package io.github.nomisrev

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropertyTesting
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.long

@Serializable
data class City(val streets: List<Street>)

@Serializable
data class Street(val name: String)

class JsonDSLSpec : StringSpec({

  PropertyTesting.defaultIterationCount = 50

  "bool prism" {
    checkAll(Arb.jsBoolean()) { jsBool ->
      JsonPath.boolean.getOrNull(jsBool) shouldBe jsBool.boolean
    }

    JsonPath.boolean.getOrNull(JsonPrimitive("text")).shouldBeNull()
  }

  "string prism" {
    checkAll(Arb.jsString()) { jsString ->
      JsonPath.string.getOrNull(jsString) shouldBe jsString.content
    }

    JsonPath.string.getOrNull(JsonPrimitive(false)).shouldBeNull()
  }

  "long prism" {
    checkAll(Arb.jsLong()) { jsLong ->
      JsonPath.long.getOrNull(jsLong) shouldBe jsLong.long
    }

    JsonPath.long.getOrNull(JsonPrimitive(false)).shouldBeNull()
  }

  "float prism" {
    checkAll(Arb.jsFloat()) { jsFloat ->
      JsonPath.float.getOrNull(jsFloat) shouldBe jsFloat.float
    }

    JsonPath.float.getOrNull(JsonPrimitive(false)).shouldBeNull()
  }

  "int prism" {
    checkAll(Arb.jsInt()) { jsInt ->
      JsonPath.int.getOrNull(jsInt) shouldBe jsInt.int
    }

    JsonPath.int.getOrNull(JsonPrimitive("text")).shouldBeNull()
  }

  "array prism" {
    checkAll(Arb.jsArray()) { jsArray ->
      JsonPath.array.getOrNull(jsArray) shouldBe jsArray
    }

    JsonPath.array.getOrNull(JsonPrimitive("5")).shouldBeNull()
  }

  "object prism" {
    checkAll(Arb.jsObject()) { jsObj ->
      JsonPath.`object`.getOrNull(jsObj) shouldBe jsObj
    }

    JsonPath.`object`.getOrNull(JsonPrimitive("5")).shouldBeNull()
  }

  "null prism" {
    checkAll(Arb.jsNull()) { jsNull ->
      JsonPath.`null`.getOrNull(jsNull) shouldBe jsNull
    }

    JsonPath.`null`.getOrNull(JsonPrimitive("5")).shouldBeNull()
  }

  "at from object" {
    checkAll(Arb.json(Arb.city())) { cityJson ->
      JsonPath.at("streets").getOrNull(cityJson)?.orNull() shouldBe (cityJson as? JsonObject)?.get("streets")
    }
  }

  "select from object" {
    checkAll(Arb.json(Arb.city())) { cityJson ->
      JsonPath.select("streets").getOrNull(cityJson) shouldBe (cityJson as? JsonObject)?.get("streets")
    }
  }

//  "extract from object" {
//    checkAll(Arb.json(Arb.city())) { cityJson ->
//      JsonPath.extract<City>().getOrNull(cityJson) shouldBe Json.decodeFromJsonElement<City>(cityJson)
//    }
//  }

//  "get from array" {
//    checkAll(Arb.json(Arb.city())) { cityJson ->
//      JsonPath.select("streets")[0]
//        .extract<Street>()
//        .getOrNull(cityJson) shouldBe Json.decodeFromJsonElement<City>(cityJson).streets.getOrNull(0)
//    }
//  }
})