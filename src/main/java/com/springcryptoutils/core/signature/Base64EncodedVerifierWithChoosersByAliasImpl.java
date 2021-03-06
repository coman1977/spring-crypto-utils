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

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.springcryptoutils.core.key.PublicKeyChooserByAlias;
import com.springcryptoutils.core.key.PublicKeyRegistryByAlias;
import com.springcryptoutils.core.keystore.KeyStoreChooser;

/**
 * The default implementation for verifying the authenticity of messages using
 * base64 encoded digital signatures when the public key alias can be configured
 * on the side of the user of this interface.
 *
 * @author Mirko Caserta (mirko.caserta@gmail.com)
 */
public class Base64EncodedVerifierWithChoosersByAliasImpl implements Base64EncodedVerifierWithChoosersByAlias {

	private Map<String, Base64EncodedVerifier> cache = new HashMap<String, Base64EncodedVerifier>();

	private PublicKeyRegistryByAlias publicKeyRegistryByAlias;

	private String algorithm = "SHA1withRSA";

	private String charsetName = "UTF-8";

	private String provider;

	/**
	 * Sets the public key registry.
	 *
	 * @param publicKeyRegistryByAlias the public key registry
	 */
	public void setPublicKeyRegistryByAlias(PublicKeyRegistryByAlias publicKeyRegistryByAlias) {
		this.publicKeyRegistryByAlias = publicKeyRegistryByAlias;
	}

	/**
	 * The signature algorithm. The default is SHA1withRSA.
	 *
	 * @param algorithm the signature algorithm
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * The charset to use when converting a string into a raw byte array
	 * representation. The default is UTF-8.
	 *
	 * @param charsetName the charset name (default: UTF-8)
	 */
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	/**
	 * Sets the provider name of the specific implementation requested (e.g.,
	 * "BC" for BouncyCastle, "SunJCE" for the default Sun JCE provider).
	 *
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * Verifies the authenticity of a message using a base64 encoded digital
	 * signature.
	 *
	 * @param keyStoreChooser the keystore chooser
	 * @param publicKeyChooserByAlias the public key chooser
	 * @param message the message to sign
	 * @param signature the base64 encoded digital signature
	 * @return true if the authenticity of the message is verified by the
	 *         digital signature
	 */
	public boolean verify(KeyStoreChooser keyStoreChooser, PublicKeyChooserByAlias publicKeyChooserByAlias, String message,
			String signature) {
		Base64EncodedVerifier verifier = cache.get(cacheKey(keyStoreChooser, publicKeyChooserByAlias));

		if (verifier != null) {
			return verifier.verify(message, signature);
		}

		Base64EncodedVerifierImpl verifierImpl = new Base64EncodedVerifierImpl();
		verifierImpl.setAlgorithm(algorithm);
		verifierImpl.setCharsetName(charsetName);
		verifierImpl.setProvider(provider);
		PublicKey publicKey = publicKeyRegistryByAlias.get(keyStoreChooser, publicKeyChooserByAlias);

		if (publicKey == null) {
			throw new SignatureException("public key not found: keyStoreName=" + keyStoreChooser.getKeyStoreName()
					+ ", alias=" + publicKeyChooserByAlias.getAlias());
		}

		verifierImpl.setPublicKey(publicKey);
		cache.put(cacheKey(keyStoreChooser, publicKeyChooserByAlias), verifierImpl);
		return verifierImpl.verify(message, signature);
	}

	private static String cacheKey(KeyStoreChooser keyStoreChooser, PublicKeyChooserByAlias publicKeyChooserByAlias) {
		return new StringBuffer().append(keyStoreChooser.getKeyStoreName()).append('-').append(
				publicKeyChooserByAlias.getAlias()).toString();
	}

}