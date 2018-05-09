/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.slugbuilder

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main {

  private implicit val system: ActorSystem = ActorSystem()
  private implicit val mat: ActorMaterializer = ActorMaterializer()

  private val slugBuilder = new SlugBuilder(
    new SlugChecker(StandaloneAhcWSClient(), "webstoreUri", "0.5.2"),
    new ArtifactChecker(),
    new ProgressReporter())

  def main(args: Array[String]): Unit =
    Await.result(
        slugBuilder.create(args(0), args(1)).value,
        atMost = 2 minutes)
      .fold(
        _ => System.exit(1),
        _ => System.exit(0)
      )
}