/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.utilitarios;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Luiz Guilherme
 */
public class Criptografia {

	public String Criptografar(String senhaNormal) {
		String senhaSegura;
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte messageDigestSenhaAdmin[] = null;
		try {
			messageDigestSenhaAdmin = algorithm.digest(senhaNormal.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuilder hexStringSenhaAdmin = new StringBuilder();
		for (byte b : messageDigestSenhaAdmin) {
			hexStringSenhaAdmin.append(String.format("%02X", 0xFF & b));
		}
		senhaSegura = hexStringSenhaAdmin.toString();

		return senhaSegura;

	}

    public static String descriptar(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigestSenhaAdmin[] = algorithm.digest(senha.getBytes("UTF-8"));

        StringBuilder hexStringSenhaAdmin = new StringBuilder();
        for (byte b : messageDigestSenhaAdmin) {
            hexStringSenhaAdmin.append(String.format("%02X", 0xFF & b));
        }
        String senhahexAdmin = hexStringSenhaAdmin.toString();

        return senhahexAdmin;

    }

}
