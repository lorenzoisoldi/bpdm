/*******************************************************************************
 * Copyright (c) 2021,2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.eclipse.tractusx.bpdm.gate.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.response.AddressResponse
import org.eclipse.tractusx.bpdm.common.dto.response.SiteResponse

@JsonDeserialize(using = SiteGateOutputDeserializer::class)
@Schema(name = "Site Gate Output", description = "Site with legal entity reference.")
data class SiteGateOutput(
    @JsonUnwrapped
    val site: SiteResponse,
    @Schema(description = "Main address where this site resides")
    val mainAddress: AddressResponse,
    @Schema(description = "ID the record has in the external system where the record originates from")
    val externalId: String,
    @Schema(description = "Business Partner Number, main identifier value for sites")
    val bpn: String?,
    @Schema(description = "Bpn of the related legal entity")
    val legalEntityBpn: String,
)

class SiteGateOutputDeserializer(vc: Class<SiteGateOutput>?) : StdDeserializer<SiteGateOutput>(vc) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SiteGateOutput {
        val node = parser.codec.readTree<JsonNode>(parser)
        return SiteGateOutput(
            ctxt.readTreeAsValue(node, SiteResponse::class.java),
            ctxt.readTreeAsValue(node.get(SiteGateOutput::mainAddress.name), AddressResponse::class.java),
            node.get(SiteGateOutput::externalId.name).textValue(),
            node.get(SiteGateOutput::bpn.name)?.textValue(),
            node.get(SiteGateOutput::legalEntityBpn.name).textValue()
        )
    }
}