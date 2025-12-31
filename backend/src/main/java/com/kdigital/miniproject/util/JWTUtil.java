package com.kdigital.miniproject.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;

public class JWTUtil {
	private static final long ACCESS_TOKEN_MSEC = 24 * 60 * (60 * 1000);	// 1일
	private static final String JWT_KEY = "com.kdigital.miniproject.jwtkey";
	
	public static final String prefix = "Bearer ";
	public static final String usernoClaim = "Userno";
	public static final String useridClaim = "Userid";
	public static final String usernameClaim = "Username";
	public static final String emailClaim = "Email";
	public static final String roleClaim = "Role";
	public static final String enabledClaim = "Enabled";
	
	private static String getJWTSource(String token) {
		if(token.startsWith(prefix)) return token.replace(prefix, "");
		return token;
	}
	
	public static String getJWT(Long userno, String userid, String username, String email, Role role, Boolean enabled) {
		String src = JWT.create()
				.withClaim(usernoClaim, userno)
				.withClaim(useridClaim, userid)
				.withClaim(usernameClaim, username)
				.withClaim(emailClaim, email)
				.withClaim(roleClaim, role.toString())
				.withClaim(enabledClaim, enabled ? "True" : "False")
				.withExpiresAt(new Date(System.currentTimeMillis()+ACCESS_TOKEN_MSEC))
				.sign(Algorithm.HMAC256(JWT_KEY));
		return prefix + src;
	}
	public static String getJWT(Member member)
	{
		return getJWT(member.getUser_no(), member.getUserid(), member.getUsername(), member.getEmail(), member.getRole(), member.getEnabled());
	}
	
	// JWT에서 Claim 추출할 때 호출
	public static String getClaim(String token, String cname) {
		String tok = getJWTSource(token);
		Claim claim = JWT.require(Algorithm.HMAC256(JWT_KEY)).build()
						.verify(tok).getClaim(cname);
		if (claim.isMissing()) return null;
		return claim.asString();
	}
	
	public static boolean isExpired(String token) {
		String tok = getJWTSource(token);
		return JWT.require(Algorithm.HMAC256(JWT_KEY)).build()
						.verify(tok).getExpiresAt().before(new Date());
	}

}
