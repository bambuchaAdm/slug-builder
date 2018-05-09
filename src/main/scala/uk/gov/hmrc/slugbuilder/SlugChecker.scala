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

import cats.data.EitherT
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal

class SlugChecker(wSClient: StandaloneWSClient, webstoreUri: String, slugBuilderVersion: String) {

  def checkIfDoesNotExist(
    repositoryName: RepositoryName,
    releaseVersion: ReleaseVersion): EitherT[Future, String, String] = {
    val url = s"$webstoreUri/slugs/$repositoryName/${repositoryName}_${releaseVersion}_$slugBuilderVersion.tgz"

    EitherT[Future, String, String] {
      wSClient
        .url(url)
        .get()
        .map(_.status)
        .map {
          case 200    => Left(s"Slug already exists at: $url")
          case 404    => Right("Slug does not exist")
          case status => Left(s"Cannot check if slug exists at $url. Returned status $status")
        }
        .recover {
          case NonFatal(exception) =>
            Left(s"Cannot check if slug exists at $url. Got exception: ${exception.getMessage}")
          case other => throw other
        }
    }
  }
}