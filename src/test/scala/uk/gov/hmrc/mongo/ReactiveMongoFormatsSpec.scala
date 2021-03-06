/*
 * Copyright 2014 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.hmrc.mongo

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsUndefined, JsNumber, JsString, Json}

class ReactiveMongoFormatsSpec extends WordSpec with Matchers {

  case class Foo(id: String, name: String, _age: Int)


  "mongoEntity" should {

    "replace _id with id when reading json" in {

      val json = Json.obj("_id" -> JsString("id"), "name" -> JsString("name"), "_age" -> JsNumber(22), "id" -> "ignored")

      val defaultFormat = Json.format[Foo]
      val entity = ReactiveMongoFormats.mongoEntity(defaultFormat).reads(json).get
      entity shouldBe Foo("id", "name", 22)
    }

    "replace id with _id when writing json" in {
      val entity = Foo("id", "name", 22)
      val defaultFormat = Json.format[Foo]
      val json = ReactiveMongoFormats.mongoEntity(defaultFormat).writes(entity)
      json \ "_id" shouldBe JsString("id")
      json \ "name" shouldBe JsString("name")
      json \ "_age" shouldBe JsNumber(22)
      json \ "id" shouldBe a[JsUndefined]

    }

  }

}
