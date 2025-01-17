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

package org.eclipse.tractusx.bpdm.common.model

enum class AddressType(private val typeName: String, private val url: String) : NamedUrlType, HasDefaultValue<AddressType> {
    BRANCH_OFFICE("Branch Office", ""),
    CARE_OF("Care of (c/o) Address", ""),
    HEADQUARTER("Headquarter", ""),
    LEGAL("Legal", ""),
    LEGAL_ALTERNATIVE("Legal Alternative", ""),
    PO_BOX("Post Office Box", ""),
    REGISTERED("Registered", ""),
    REGISTERED_AGENT_MAIL("Registered Agent Mail", ""),
    REGISTERED_AGENT_PHYSICAL("Registered Agent Physical", ""),
    VAT_REGISTERED("Vat Registered", ""),
    UNSPECIFIC("Unspecified", "");

    override fun getTypeName(): String {
        return typeName
    }

    override fun getUrl(): String {
        return url
    }

    override fun getDefault(): AddressType {
        return UNSPECIFIC
    }
}