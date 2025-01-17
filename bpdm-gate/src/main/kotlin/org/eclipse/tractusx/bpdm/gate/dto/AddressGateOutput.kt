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

@JsonDeserialize(using = AddressGateOutputDeserializer::class)
@Schema(
    name = "Address Gate Output", description = "Address with legal entity or site references. " +
            "Only one of either legal entity or site external id can be set for an address."
)
data class AddressGateOutput(
    @Schema(description = "Business Partner Number")
    val bpn: String? = null,
    @JsonUnwrapped
    val address: AddressResponse,
    @Schema(description = "ID the record has in the external system where the record originates from")
    val externalId: String,
    @Schema(description = "External id of the related legal entity")
    val legalEntityBpn: String? = null,
    @Schema(description = "External id of the related site")
    val siteBpn: String? = null
)

class AddressGateOutputDeserializer(vc: Class<AddressGateOutput>?) : StdDeserializer<AddressGateOutput>(vc) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): AddressGateOutput {
        val node = parser.codec.readTree<JsonNode>(parser)
        return AddressGateOutput(
            node.get(AddressGateOutput::bpn.name)?.textValue(),
            ctxt.readTreeAsValue(node, AddressResponse::class.java),
            node.get(AddressGateOutput::externalId.name).textValue(),
            node.get(AddressGateOutput::legalEntityBpn.name)?.textValue(),
            node.get(AddressGateOutput::siteBpn.name)?.textValue()
        )
    }
}