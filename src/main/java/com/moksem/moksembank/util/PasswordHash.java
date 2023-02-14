package com.moksem.moksembank.util;

import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Class for hashing of passwords
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordHash {
    /**
     * Hashing password
     *
     * @param pass  current password
     * @return      object of {@link String}
     */
    public static String encode(String pass){
        return pass != null? DigestUtils.md5Hex(pass):"";
    }

    /**
     * Validate matching of passwords
     *
     * @param pass  password
     * @param hash  hashing password
     * @throws InvalidLoginOrPasswordException Invalid login or password exception
     */
    public static void verify(String pass, String hash) throws InvalidLoginOrPasswordException {
        if(!encode(pass).equals(hash))
            throw new InvalidLoginOrPasswordException();
    }
}
