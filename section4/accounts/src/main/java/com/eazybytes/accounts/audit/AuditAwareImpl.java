package com.eazybytes.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Author: ldeepak
 */

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("ACCOUNTS_MS"); // Hardcoded the user name for simplicity, can be dynamic based on context
	}
}
