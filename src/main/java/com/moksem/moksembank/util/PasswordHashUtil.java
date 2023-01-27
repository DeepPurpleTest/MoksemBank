package com.moksem.moksembank.util;

import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordHashUtil {
    public static String encode(String pass){
//        System.out.println(DigestUtils.md5Hex(pass));
        return pass != null? DigestUtils.md5Hex(pass):"";
    }

    public static void verify(String pass, String hash) throws InvalidLoginOrPasswordException {
        if(!encode(pass).equals(hash))
            throw new InvalidLoginOrPasswordException();
    }
}
