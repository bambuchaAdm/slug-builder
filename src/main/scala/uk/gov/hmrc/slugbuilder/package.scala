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

package uk.gov.hmrc

case class RepositoryName(value: String) {
  if (value.trim.isEmpty) throw new IllegalArgumentException("Blank repository name not allowed")
}

case class ReleaseVersion(value: String) {
  private val versionPattern = """(\d+)\.(\d+)\.(\d+)""".r

  if (value.trim.isEmpty) throw new IllegalArgumentException("Blank release version not allowed")

  val (major, minor, patch) = value match {
    case versionPattern(majorVersion, minorVersion, patchVersion) => (majorVersion, minorVersion, patchVersion)
    case _                                                        => throw new IllegalArgumentException(s"$value is not in 'NNN.NNN.NNN' format")
  }

  override val toString: String = value
}
