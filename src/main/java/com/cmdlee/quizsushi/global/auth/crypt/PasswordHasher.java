package com.cmdlee.quizsushi.global.tmp.security;

public interface PasswordHasher {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}
