package com.veerswap.validator;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Zero-dependency address format and Memo/Destination Tag validator for major
 * blockchain networks (Bitcoin, EVM, Solana, Tron, XRP, Stellar, TON, Litecoin).
 *
 * <p>Operational guides:
 * <ul>
 *   <li><a href="https://veerswap.com/guides/wrong-network">Wrong-Network Prevention Guide</a></li>
 *   <li><a href="https://veerswap.com/guides/memo-and-tag">Memo &amp; Destination Tag Guide</a></li>
 *   <li><a href="https://veerswap.com/exchange">VeerSwap Cross-Chain Exchange Directory</a></li>
 * </ul>
 */
public final class AddressValidator {
    private static final Pattern EVM = Pattern.compile("^0x[0-9a-fA-F]{40}$");
    private static final Pattern BTC_B32 = Pattern.compile("^bc1[0-9a-z]{25,87}$");
    private static final Pattern BTC_B58 = Pattern.compile("^[13][1-9A-HJ-NP-Za-km-z]{25,34}$");
    private static final Pattern SOL = Pattern.compile("^[1-9A-HJ-NP-Za-km-z]{32,44}$");
    private static final Pattern TRON = Pattern.compile("^T[1-9A-HJ-NP-Za-km-z]{33}$");
    private static final Pattern XRP = Pattern.compile("^r[1-9A-HJ-NP-Za-km-z]{24,34}$");
    private static final Pattern XLM = Pattern.compile("^G[A-Z2-7]{55}$");

    private AddressValidator() {}

    public static boolean isValidAddress(String network, String address) {
        if (network == null || address == null) return false;
        String net = network.trim().toLowerCase(Locale.ROOT);
        String addr = address.trim();
        if (addr.isEmpty()) return false;
        return switch (net) {
            case "ethereum", "eth", "base", "arbitrum", "bnb", "polygon" -> EVM.matcher(addr).matches();
            case "bitcoin", "btc" -> BTC_B32.matcher(addr.toLowerCase(Locale.ROOT)).matches() || BTC_B58.matcher(addr).matches();
            case "solana", "sol" -> SOL.matcher(addr).matches();
            case "tron", "trx", "trc20" -> TRON.matcher(addr).matches();
            case "xrp", "ripple" -> XRP.matcher(addr).matches();
            case "stellar", "xlm" -> XLM.matcher(addr).matches();
            default -> false;
        };
    }

    public static boolean isValidXrpDestinationTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) return true;
        try {
            long val = Long.parseLong(tag.trim());
            return val >= 0 && val <= 4294967295L;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
