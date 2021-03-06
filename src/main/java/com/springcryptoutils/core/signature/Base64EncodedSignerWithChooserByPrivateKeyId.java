/*
 * Copyright 2012 Mirko Caserta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springcryptoutils.core.signature;

/**
 * An interface for providing base64 encoded versions of digital signatures
 * when the private key is configured in an underlying mapping using a logical name.
 *
 * @author Mirko Caserta (mirko.caserta@gmail.com)
 */
public interface Base64EncodedSignerWithChooserByPrivateKeyId {

    /**
     * Signs a message.
     *
     * @param privateKeyId the logical name of the private key as configured
     *                     in the underlying mapping
     * @param message      the message to sign
     * @return a base64 encoded version of the signature
     */
    String sign(String privateKeyId, String message);

}