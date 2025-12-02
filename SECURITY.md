# Security Policy

Thank you for taking the time to help keep Extra Wallet secure.  
This document outlines our policies for reporting vulnerabilities, security expectations, and responsible disclosure.

## Supported Versions

Security fixes are applied to the following versions:

| Version | Supported |
|--------|-----------|
| Latest main branch | ✔ |
| Latest stable release | ✔ |
| Older releases | ✖ (report anyway if critical) |

If you're using an older version, please update to ensure you receive the latest security improvements.

## Reporting a Vulnerability

If you discover a security vulnerability, **do not create a public issue**.

Instead, please report it privately via:

- **Security Contact:** `trihajikhr@gmail.com`  
or  
- **GitHub Private Advisory:** Use “Report a vulnerability” in the repository’s Security tab.

When reporting, please include:

- A detailed description of the issue  
- Steps to reproduce  
- Impact assessment (what could happen if exploited)  
- Suggested fix (optional but appreciated)  
- Any proof-of-concept code or screenshots

You can expect:

- A response within **72 hours**  
- A severity assessment  
- A timeline for the fix  
- A coordinated disclosure process

## Responsible Disclosure

We request that you:

- Allow us adequate time to investigate and patch the issue  
- Avoid publicly discussing the vulnerability before a fix is released  
- Do not exploit the vulnerability beyond what is necessary for the report

We will credit contributors who responsibly report vulnerabilities unless they prefer to remain anonymous.

## Security Practices

Extra Wallet follows these internal security guidelines:

- All changes to protected branches require code review  
- No direct pushes to `main`  
- Secrets, tokens, and credentials are never committed to the repository  
- SQLite databases used for development should **not** contain real personal or financial data  
- User data is handled securely and not logged unnecessarily  
- Dependencies are regularly scanned for vulnerabilities

## Encryption & Data Safety

Extra Wallet handles personal finance data. We strongly advise:

- Users protect their local database (`.sqlite`) with OS-level security  
- Developers never share sample databases with real user data  
- API keys or sensitive config values must be stored in environment variables, not in code

## Thanks

We appreciate security researchers and contributors who help keep Extra Wallet safe for everyone.

If you have questions regarding security or safe usage, feel free to contact us.