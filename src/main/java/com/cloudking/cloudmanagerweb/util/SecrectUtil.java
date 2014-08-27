/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Dec 15, 2012  12:59:28 AM
 */
package com.cloudking.cloudmanagerweb.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;

/**
 * 对称加密实现
 * 
 * @author CloudKing
 */
public class SecrectUtil {
    /**
     * 密钥
     */
    private static final byte[] KEY = Hex.decode("1c1f01e3a11694f2fbb5eab62562abaece2cc88354d0d654");

    /**
     * 加密。如果加密失败就返回null
     * 
     * @param value
     * @return
     */
    public static String encrypt(String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return new String(Hex.encode(cipher.doFinal(value.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.error(e);
        } catch (NoSuchPaddingException e) {
            LogUtil.error(e);
        } catch (InvalidKeyException e) {
            LogUtil.error(e);
        } catch (IllegalBlockSizeException e) {
            LogUtil.error(e);
        } catch (BadPaddingException e) {
            LogUtil.error(e);
        }
        return null;
    }

    /**
     * 解密。如果加密失败就返回null
     * 
     * @param value
     * @return
     */
    public static String decrypt(String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        try {

            byte[] tmp = Hex.decode(value.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(tmp));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.error(e);
        } catch (NoSuchPaddingException e) {
            LogUtil.error(e);
        } catch (InvalidKeyException e) {
            LogUtil.error(e);
        } catch (IllegalBlockSizeException e) {
            LogUtil.error(e);
        } catch (BadPaddingException e) {
            LogUtil.error(e);
        }
        return null;
    }
}
