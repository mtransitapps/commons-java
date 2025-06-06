package org.mtransit.commons;

import static org.mtransit.commons.Constants.EMPTY;
import static org.mtransit.commons.RegexUtils.ALPHA_NUM_CAR;
import static org.mtransit.commons.RegexUtils.ANY;
import static org.mtransit.commons.RegexUtils.BEGINNING;
import static org.mtransit.commons.RegexUtils.END;
import static org.mtransit.commons.RegexUtils.NON_WORD_CAR;
import static org.mtransit.commons.RegexUtils.WHITESPACE_CAR;
import static org.mtransit.commons.RegexUtils.atLeastOne;
import static org.mtransit.commons.RegexUtils.group;
import static org.mtransit.commons.RegexUtils.mGroup;
import static org.mtransit.commons.RegexUtils.or;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class CleanUtils {

	private CleanUtils() {
	}

	public static final Pattern CLEAN_EN_DASHES = Pattern.compile("(\\w)\\s*–\\s*(\\w)");
	public static final String CLEAN_EN_DASHES_REPLACEMENT = "$1–$2";
	public static final Pattern CLEAN_DASHES = Pattern.compile("(\\w)\\s*-\\s*(\\w)");
	public static final String CLEAN_DASHES_REPLACEMENT = "$1-$2";

	private static final String PARENTHESIS1 = "\\(";
	public static final Pattern CLEAN_PARENTHESIS1 = Pattern.compile(PARENTHESIS1 + "\\s*(\\w)");
	public static final String CLEAN_PARENTHESIS1_REPLACEMENT = "($1";
	@Deprecated
	private static final String PARENTHESE1 = PARENTHESIS1;
	@Deprecated
	public static final Pattern CLEAN_PARENTHESE1 = CLEAN_PARENTHESIS1;
	@Deprecated
	public static final String CLEAN_PARENTHESE1_REPLACEMENT = CLEAN_PARENTHESIS1_REPLACEMENT;

	private static final String PARENTHESIS2 = "\\)";

	public static final Pattern CLEAN_PARENTHESIS2 = Pattern.compile("(\\w)\\s*" + PARENTHESIS2);

	public static final String CLEAN_PARENTHESIS2_REPLACEMENT = "$1)";

	@Deprecated
	private static final String PARENTHESE2 = PARENTHESIS2;
	@Deprecated
	public static final Pattern CLEAN_PARENTHESE2 = CLEAN_PARENTHESIS2;
	@Deprecated
	public static final String CLEAN_PARENTHESE2_REPLACEMENT = CLEAN_PARENTHESIS2_REPLACEMENT;

	private static final Pattern CLEAN_SPACES = Pattern.compile("\\s+");
	private static final Pattern CLEAN_P1 = Pattern.compile("\\s*\\(\\s*");
	private static final String CLEAN_P1_REPLACEMENT = " ("; // space: guaranty 1 before & 0 after
	private static final Pattern CLEAN_P2 = Pattern.compile("\\s*\\)\\s*");
	private static final String CLEAN_P2_REPLACEMENT = ") "; // space: guaranty 0 before & 1 after

	/**
	 * @deprecated use {@link #cleanLabel(Locale, String)}
	 */
	@Deprecated
	@NotNull
	public static String cleanLabel(@NotNull String label) {
		return cleanLabel(Locale.ENGLISH, label);
	}

	private static final char[] CAPITALIZE_CHARS_EN = new char[]{
			' ',
			'-', '–',
			'/',
			'(',
			'.'
	};

	private static final char[] CAPITALIZE_CHARS_FR = new char[]{
			' ',
			'-', '–',
			'/',
			'(',
			'.',
			'\'', '`', '’',
			':'
	};

	@NotNull
	public static String cleanLabel(@NotNull Locale locale, @NotNull String label) {
		label = CLEAN_SPACES.matcher(label).replaceAll(SPACE);
		label = CLEAN_P1.matcher(label).replaceAll(CLEAN_P1_REPLACEMENT);
		label = CLEAN_P2.matcher(label).replaceAll(CLEAN_P2_REPLACEMENT);
		if (locale == Locale.FRENCH) {
			label = WordUtils.capitalize(label, CAPITALIZE_CHARS_FR);
		} else {
			label = WordUtils.capitalize(label, CAPITALIZE_CHARS_EN);
		}
		label = removePointsI(label); // after capitalize
		return label.trim();
	}

	private static final String PLACE_CHAR_DE_L = "de l'";
	private static final String PLACE_CHAR_DE_LA = "de la ";
	private static final String PLACE_CHAR_D = "d'";
	private static final String PLACE_CHAR_DE = "de ";
	private static final String PLACE_CHAR_DES = "des ";
	private static final String PLACE_CHAR_DU = "du ";
	private static final String PLACE_CHAR_LA = "la ";
	private static final String PLACE_CHAR_LE = "le ";
	private static final String PLACE_CHAR_LES = "les ";
	private static final String PLACE_CHAR_L = "l'";

	private static final Pattern[] START_WITH_CHARS = new Pattern[]{ //
			Pattern.compile("^(" + PLACE_CHAR_DE_L + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_DE_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_D + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_DE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_DES + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_DU + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_LE + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("^(" + PLACE_CHAR_LES + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("^(" + PLACE_CHAR_L + ")", Pattern.CASE_INSENSITIVE) //
	};

	public static final Pattern[] SPACE_CHARS = new Pattern[]{ //
			Pattern.compile("( " + PLACE_CHAR_DE_L + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_DE_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_D + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_DE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_DES + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_DU + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_LE + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("( " + PLACE_CHAR_LES + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("( " + PLACE_CHAR_L + ")", Pattern.CASE_INSENSITIVE) //
	};

	private static final Pattern[] SLASH_CHARS = new Pattern[]{//
			Pattern.compile("(/ " + PLACE_CHAR_DE_L + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_DE_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_D + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_DE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_DES + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_DU + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_LA + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_LE + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("(/ " + PLACE_CHAR_LES + ")", Pattern.CASE_INSENSITIVE),//
			Pattern.compile("(/ " + PLACE_CHAR_L + ")", Pattern.CASE_INSENSITIVE) //
	};

	private static final String PLACE_CHAR_ARRONDISSEMENT = "arrondissement ";
	private static final String PLACE_CHAR_AV = "av ";
	private static final String PLACE_CHAR_AVENUE = "avenue ";
	private static final String PLACE_CHAR_BOUL = "boul ";
	private static final String PLACE_CHAR_BOULEVARD = "boulevard ";
	private static final String PLACE_CHAR_CH = "ch ";
	private static final String PLACE_CHAR_CIVIQUE = "civique ";
	private static final String PLACE_CHAR_CROISS = "croiss ";
	private static final String PLACE_CHAR_QUARTIER = "quartier ";
	private static final String PLACE_CHAR_RTE = "rte ";
	private static final String PLACE_CHAR_RUE = "rue ";
	private static final String PLACE_CHAR_TSSE = "tsse ";

	private static final Pattern[] START_WITH_ST = new Pattern[]{ //
			Pattern.compile("^(" + PLACE_CHAR_ARRONDISSEMENT + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_AV + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_AVENUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_BOUL + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_BOULEVARD + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_CH + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_CIVIQUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_CROISS + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_QUARTIER + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_RTE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_RUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^(" + PLACE_CHAR_TSSE + ")", Pattern.CASE_INSENSITIVE) //
	};

	public static final Pattern[] SPACE_ST = new Pattern[]{ //
			Pattern.compile("( " + PLACE_CHAR_ARRONDISSEMENT + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_AV + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_AVENUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_BOUL + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_BOULEVARD + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_CH + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_CIVIQUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_CROISS + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_QUARTIER + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_RTE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_RUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("( " + PLACE_CHAR_TSSE + ")", Pattern.CASE_INSENSITIVE) //
	};

	private static final Pattern[] SLASH_ST = new Pattern[]{ //
			Pattern.compile("(/ " + PLACE_CHAR_ARRONDISSEMENT + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_AV + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_AVENUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_BOUL + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_BOULEVARD + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_CH + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_CIVIQUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_CROISS + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_QUARTIER + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_RTE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_RUE + ")", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("(/ " + PLACE_CHAR_TSSE + ")", Pattern.CASE_INSENSITIVE) //
	};

	@NotNull
	public static Pattern cleanWord(@NotNull String word) {
		return cleanWords(word);
	}

	@NotNull
	public static Pattern cleanWords(@Nullable String... words) {
		return cleanWords(Pattern.CASE_INSENSITIVE, words);
	}

	@NotNull
	public static Pattern cleanWordFR(@NotNull String word) {
		return cleanWordsFR(word);
	}

	@NotNull
	public static Pattern cleanWordsFR(@Nullable String... words) {
		return cleanWords(Pattern.CASE_INSENSITIVE | RegexUtils.fUNICODE_CHARACTER_CLASS() | RegexUtils.fCANON_EQ(), words);
	}

	@NotNull
	public static Pattern cleanWord(int flags, @NotNull String word) {
		return cleanWords(flags, word);
	}

	@NotNull
	public static Pattern cleanWords(int flags, @Nullable String... words) {
		if (words == null || words.length == 0) {
			throw new RuntimeException("Cannot clean empty list of words!");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append("(?<=(^|\\W))");
		sb.append("(");
		boolean firstWorld = true;
		for (String word : words) {
			if (!firstWorld) {
				sb.append('|');
			}
			sb.append(word);
			firstWorld = false;
		}
		sb.append(")");
		sb.append("(?=(\\W|$))");
		sb.append(")");
		//noinspection MagicConstant
		return Pattern.compile(sb.toString(), flags);
	}

	@NotNull
	public static String cleanWordsReplacement(@Nullable String replacement) {
		if (replacement == null || replacement.isEmpty()) {
			return EMPTY;
		}
		return replacement;
	}

	@NotNull
	public static Pattern cleanWordPlural(@NotNull String word) {
		return cleanWordsPlural(word);
	}

	@NotNull
	public static Pattern cleanWordsPlural(@Nullable String... words) {
		return cleanWordsPlural(Pattern.CASE_INSENSITIVE, words);
	}

	@NotNull
	public static Pattern cleanWordPluralFR(@NotNull String word) {
		return cleanWordsPluralFR(word);
	}

	@NotNull
	public static Pattern cleanWordsPluralFR(@Nullable String... words) {
		return cleanWordsPlural(Pattern.CASE_INSENSITIVE | RegexUtils.fUNICODE_CHARACTER_CLASS() | RegexUtils.fCANON_EQ(), words);
	}

	@NotNull
	public static Pattern cleanWordPlural(int flags, @NotNull String word) {
		return cleanWordsPlural(flags, word);
	}

	@NotNull
	public static Pattern cleanWordsPlural(int flags, @Nullable String... words) {
		if (words == null || words.length == 0) {
			throw new RuntimeException("Cannot clean empty list of words!");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append("(?<=(^|\\W))");
		sb.append("(");
		sb.append("(");
		boolean firstWorld = true;
		for (String word : words) {
			if (!firstWorld) {
				sb.append('|');
			}
			sb.append(word);
			firstWorld = false;
		}
		sb.append(")");
		sb.append("(s)?");
		sb.append(")");
		sb.append("(?=(\\W|$))");
		sb.append(")");
		//noinspection MagicConstant
		return Pattern.compile(sb.toString(), flags);
	}

	@NotNull
	public static String cleanWordsReplacementPlural(@Nullable String replacement) {
		if (replacement == null || replacement.isEmpty()) {
			return EMPTY;
		}
		return replacement + "$5";
	}

	public static final Pattern SAINT = Pattern.compile("(saint)", Pattern.CASE_INSENSITIVE);

	public static final String SAINT_REPLACEMENT = "St";

	public static final Pattern CLEAN_AT = cleanWords("at");

	public static final String CLEAN_AT_REPLACEMENT = cleanWordsReplacement("@");

	public static final Pattern CLEAN_AND = cleanWords("and");

	public static final String CLEAN_AND_REPLACEMENT = cleanWordsReplacement("&");

	public static final Pattern CLEAN_ET = cleanWordsFR("et");

	public static final String CLEAN_ET_REPLACEMENT = CLEAN_AND_REPLACEMENT;

	public static final String SPACE = " ";

	public static final char SPACE_CHAR = ' ';

	public static final String SLASH_SPACE = "/ ";

	@NotNull
	public static String cleanLabelFR(@NotNull String label) {
		label = cleanSlashes(label);
		label = CLEAN_PARENTHESIS1.matcher(label).replaceAll(CLEAN_PARENTHESIS1_REPLACEMENT);
		label = CLEAN_PARENTHESIS2.matcher(label).replaceAll(CLEAN_PARENTHESIS2_REPLACEMENT);
		label = SAINT.matcher(label).replaceAll(SAINT_REPLACEMENT);
		label = removePointsI(label); // after capitalize
		label = RegexUtils.replaceAllNN(label.trim(), START_WITH_ST, SPACE); // Constants.EMPTY); // SPACE);
		label = RegexUtils.replaceAllNN(label, SLASH_ST, SLASH_SPACE);
		label = RegexUtils.replaceAllNN(label.trim(), START_WITH_CHARS, SPACE); // , Constants.EMPTY); //
		label = RegexUtils.replaceAllNN(label, SLASH_CHARS, SLASH_SPACE);
		return cleanLabel(Locale.FRENCH, label);
	}

	private static final Pattern CLEAN_SLASH = Pattern.compile("(\\S)\\s*/\\s*(\\S)");
	private static final String CLEAN_SLASH_REPLACEMENT = "$1" + " / " + "$2";

	@NotNull
	public static String cleanSlashes(@NotNull String string) {
		return CLEAN_SLASH.matcher(string).replaceAll(CLEAN_SLASH_REPLACEMENT);
	}

	private static final Pattern _3_POINTS = Pattern.compile("(\\.\\.\\.)");
	private static final String _3_POINTS_REPLACEMENT = "…";

	private static final Pattern POINT1_SPACE = Pattern.compile("(" +
			"(?=(^|\\s|[A-Z]+))" +
			"(\\w)\\.\\s" +
			"(?=(\\w(\\.\\s|\\.$|$)|$))" +
			")");
	private static final String POINT1_SPACE_REPLACEMENT = "$3";

	private static final Pattern POINT1 = Pattern.compile("((?=(^|\\s|[A-Z]+))(\\w)\\.(?=(\\w(\\.|\\s|$)|\\W|$)))");
	private static final String POINT1_REPLACEMENT = "$3";

	private static final Pattern POINTS = Pattern.compile("((?=(^|\\s|\\w+))(\\w+)\\.(?=(\\w{2,}\\.|\\W|$)))");
	private static final String POINTS_REPLACEMENT = "$3";

	private static final Pattern ENDS_WITH_POINTS = Pattern.compile("((\\.+)(\\W*)$)");
	private static final String ENDS_WITH_POINTS_REPLACEMENT = EMPTY;

	/**
	 * @deprecated remove call (now included in {@link #cleanLabel(String)}
	 */
	@SuppressWarnings("DeprecatedIsStillUsed") // WIP
	@Deprecated
	@NotNull
	public static String removePoints(@NotNull String capitalizedString) {
		return removePointsI(capitalizedString);
	}

	@SuppressWarnings("ProtectedMemberInFinalClass") // visible for test (?)
	@NotNull
	protected static String removePointsI(@NotNull String capitalizedString) {
		capitalizedString = _3_POINTS.matcher(capitalizedString).replaceAll(_3_POINTS_REPLACEMENT);
		capitalizedString = POINT1_SPACE.matcher(capitalizedString).replaceAll(POINT1_SPACE_REPLACEMENT);
		capitalizedString = POINT1.matcher(capitalizedString).replaceAll(POINT1_REPLACEMENT);
		capitalizedString = POINTS.matcher(capitalizedString).replaceAll(POINTS_REPLACEMENT);
		capitalizedString = ENDS_WITH_POINTS.matcher(capitalizedString).replaceAll(ENDS_WITH_POINTS_REPLACEMENT);
		return capitalizedString;
	}

	private static final Pattern BEGINNING_AND_VERS = Pattern.compile(group(
			group(or(BEGINNING, BEGINNING + atLeastOne(ANY) + NON_WORD_CAR))
					+ group(or("vers", "direction")) + NON_WORD_CAR
	), Pattern.CASE_INSENSITIVE);

	@NotNull
	public static String keepToFR(@NotNull String string) {
		string = BEGINNING_AND_VERS.matcher(string).replaceAll(EMPTY);
		return string;
	}

	private static final Pattern BEGINNING_AND_TO = Pattern.compile(group(
			group(or(BEGINNING, BEGINNING + atLeastOne(ANY) + NON_WORD_CAR))
					+ group(or("towards", "to")) + NON_WORD_CAR
	), Pattern.CASE_INSENSITIVE);

	@NotNull
	public static String keepTo(@NotNull String string) {
		string = BEGINNING_AND_TO.matcher(string).replaceAll(EMPTY);
		return string;
	}

	private static final Pattern BEGINNING_AND_VIA = Pattern.compile(group(
			group(or(BEGINNING, BEGINNING + atLeastOne(ANY) + NON_WORD_CAR))
					+ group("via") + NON_WORD_CAR
	), Pattern.CASE_INSENSITIVE);
	private static final String BEGINNING_AND_VIA_KEEP_ = "$3 ";

	@NotNull
	public static String keepVia(@NotNull String string) {
		return keepVia(string, false);
	}

	@NotNull
	public static String keepVia(@NotNull String string, boolean keepVia) {
		string = BEGINNING_AND_VIA.matcher(string).replaceAll(keepVia ? BEGINNING_AND_VIA_KEEP_ : EMPTY);
		return string;
	}

	private static final Pattern VIA_AND_ENDS = Pattern.compile(group(
			WHITESPACE_CAR + "via" + WHITESPACE_CAR + atLeastOne(ANY) + END
	), Pattern.CASE_INSENSITIVE);

	@NotNull
	public static String removeVia(@NotNull String string) {
		string = VIA_AND_ENDS.matcher(string).replaceAll(EMPTY);
		return string;
	}

	@Deprecated
	@NotNull
	public static String keepToAndRevoveVia(@NotNull String string) {
		return keepToAndRemoveVia(string);
	}

	@NotNull
	public static String keepToAndRemoveVia(@NotNull String string) {
		string = keepTo(string);
		string = removeVia(string);
		return string;
	}

	@NotNull
	public static String keepOrRemoveVia(@NotNull String tripHeadsign, @NotNull SameValidator validator) {
		final String tripHeadsignBeforeVIA = CleanUtils.removeVia(tripHeadsign);
		final String tripHeadsignAfterVIA = CleanUtils.keepVia(tripHeadsign, true);
		if (!tripHeadsignBeforeVIA.equals(tripHeadsignAfterVIA)) {
			if (validator.isSame(tripHeadsignBeforeVIA)) {
				tripHeadsign = tripHeadsignAfterVIA;
			} else if (validator.isSame(tripHeadsignAfterVIA)) {
				tripHeadsign = tripHeadsignBeforeVIA;
			} else {
				tripHeadsign = tripHeadsignBeforeVIA;
			}
		}
		return tripHeadsign;
	}

	public interface SameValidator {
		boolean isSame(@NotNull String string);
	}

	private static final String KEEP_BEFORE_ALTER = mGroup(2) + mGroup(4);

	@NotNull
	public static String removeStrings(@NotNull String string, @NotNull String... stringsToRemove) {
		//noinspection MagicConstant #AndroidCompat
		string = Pattern.compile(group(
						group(or(BEGINNING, NON_WORD_CAR)) +
								group(or(stringsToRemove)) +
								group(or(NON_WORD_CAR, END))
				), Pattern.CASE_INSENSITIVE | RegexUtils.fUNICODE_CHARACTER_CLASS() | RegexUtils.fCANON_EQ())
				.matcher(string).replaceAll(KEEP_BEFORE_ALTER);
		return string;
	}

	private static final Pattern MC_ = Pattern.compile("(" +
			"(^|\\W)" +
			"(" +
			"(" +
			"(m)" + // uppercase
			"(c|ac)" + // lowercase
			")" +
			"([a-z])" + // uppercase
			"([a-z]+)" + // lowercase
			")" +
			"(\\W|$)" +
			")", Pattern.CASE_INSENSITIVE);

	@NotNull
	public static String fixMcXCase(@NotNull String string) { // Mccowan -> McCowan
		final Matcher matcher = MC_.matcher(string);
		while (matcher.find()) {
			string = string.replaceAll(
					matcher.group(3),
					matcher.group(5).toUpperCase(Locale.ENGLISH) +
							matcher.group(6).toLowerCase(Locale.ENGLISH) +
							matcher.group(7).toUpperCase(Locale.ENGLISH) +
							matcher.group(8).toLowerCase(Locale.ENGLISH)
			);
		}
		return string;
	}

	private static final String WORD_REGEX = "a-zA-ZÀ-ÿ'"; // MARY'S

	@SuppressWarnings("MagicConstant") // #AndroidCompat
	private static final Pattern WORD_NON_WORDS = Pattern.compile(
			"([^" + WORD_REGEX + "]*)([" + WORD_REGEX + "]+)([^" + WORD_REGEX + "]*)",
			Pattern.CASE_INSENSITIVE | RegexUtils.fUNICODE_CHARACTER_CLASS() | RegexUtils.fCANON_EQ());

	private static final String WORD_REGEX_FR = "a-zA-ZÀ-ÿ"; // d'AYLMER

	@SuppressWarnings("MagicConstant") // #AndroidCompat
	private static final Pattern WORD_NON_WORDS_FR = Pattern.compile(
			"([^" + WORD_REGEX_FR + "]*)([" + WORD_REGEX_FR + "]+)([^" + WORD_REGEX_FR + "]*)",
			Pattern.CASE_INSENSITIVE | RegexUtils.fUNICODE_CHARACTER_CLASS() | RegexUtils.fCANON_EQ());

	@NotNull
	public static String toLowerCaseUpperCaseWords(@NotNull Locale locale, @NotNull String string, @NotNull String... ignoreWords) {
		if (string.isEmpty()) {
			return string;
		}
		final float charCount = string.length();
		final float upperCaseCount = CharUtils.countUpperCase(string);
		final float percent = upperCaseCount / charCount;
		if (percent < .25f) { // 25%
			return string;
		}
		StringBuilder sb = new StringBuilder();
		Pattern pattern = locale == Locale.FRENCH ? WORD_NON_WORDS_FR : WORD_NON_WORDS;
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			sb.append(matcher.group(1)); // before
			final String word = matcher.group(2);
			if (!word.isEmpty()
					&& CharUtils.isUppercaseOnly(word, false, true)
					&& !CharUtils.isRomanDigits(word)
					&& !containsIgnoreCase(word, ignoreWords)) {
				sb.append(word.toLowerCase(locale));
			} else {
				sb.append(word);
			}
			sb.append(matcher.group(3)); // after
		}
		return sb.toString();
	}

	private static boolean containsIgnoreCase(@Nullable String string, @NotNull String... ignoreWords) {
		for (String ignoreWord : ignoreWords) {
			if (ignoreWord.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	// TODO white-space VS non-word?
	private static final Pattern FIRST = cleanWords("first");
	private static final String FIRST_REPLACEMENT = cleanWordsReplacement("1st");
	private static final Pattern SECOND = cleanWords("second");
	private static final String SECOND_REPLACEMENT = cleanWordsReplacement("2nd");
	private static final Pattern THIRD = cleanWords("third");
	private static final String THIRD_REPLACEMENT = cleanWordsReplacement("3rd");
	private static final Pattern FOURTH = cleanWords("fourth");
	private static final String FOURTH_REPLACEMENT = cleanWordsReplacement("4th");
	private static final Pattern FIFTH = cleanWords("fifth");
	private static final String FIFTH_REPLACEMENT = cleanWordsReplacement("5th");
	private static final Pattern SIXTH = cleanWords("sixth");
	private static final String SIXTH_REPLACEMENT = cleanWordsReplacement("6th");
	private static final Pattern SEVENTH = cleanWords("seventh");
	private static final String SEVENTH_REPLACEMENT = cleanWordsReplacement("7th");
	private static final Pattern EIGHTH = cleanWords("eighth");
	private static final String EIGHTH_REPLACEMENT = cleanWordsReplacement("8th");
	private static final Pattern NINTH = cleanWords("ninth");
	private static final String NINTH_REPLACEMENT = cleanWordsReplacement("9th");

	private static final Pattern NO_DIGITS = Pattern.compile("((^|\\W)(no\\.? (\\d+))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String NO_DIGITS_REPLACEMENT = "$2#$4$5"; // #0

	@NotNull
	public static String cleanNumbers(@NotNull String string) {
		string = FIRST.matcher(string).replaceAll(FIRST_REPLACEMENT);
		string = SECOND.matcher(string).replaceAll(SECOND_REPLACEMENT);
		string = THIRD.matcher(string).replaceAll(THIRD_REPLACEMENT);
		string = FOURTH.matcher(string).replaceAll(FOURTH_REPLACEMENT);
		string = FIFTH.matcher(string).replaceAll(FIFTH_REPLACEMENT);
		string = SIXTH.matcher(string).replaceAll(SIXTH_REPLACEMENT);
		string = SEVENTH.matcher(string).replaceAll(SEVENTH_REPLACEMENT);
		string = EIGHTH.matcher(string).replaceAll(EIGHTH_REPLACEMENT);
		string = NINTH.matcher(string).replaceAll(NINTH_REPLACEMENT);
		string = NO_DIGITS.matcher(string).replaceAll(NO_DIGITS_REPLACEMENT);
		return string;
	}

	private static final Pattern EASTBOUND_ = cleanWords("eastbound", "eb");
	private static final String EASTBOUND_REPLACEMENT = cleanWordsReplacement("EB");
	private static final Pattern WESTBOUND_ = cleanWords("westbound", "wb");
	private static final String WESTBOUND_REPLACEMENT = cleanWordsReplacement("WB");
	private static final Pattern SOUTHBOUND_ = cleanWords("southbound", "sb");
	private static final String SOUTHBOUND_REPLACEMENT = cleanWordsReplacement("SB");
	private static final Pattern NORTHBOUND_ = cleanWords("northbound", "nb");
	private static final String NORTHBOUND_REPLACEMENT = cleanWordsReplacement("NB");

	private static final Pattern EAST_ = cleanWords("east");
	private static final String EAST_REPLACEMENT = cleanWordsReplacement("E");
	private static final Pattern WEST_ = cleanWords("west");
	private static final String WEST_REPLACEMENT = cleanWordsReplacement("W");
	private static final Pattern SOUTH_ = cleanWords("south");
	private static final String SOUTH_REPLACEMENT = cleanWordsReplacement("S");
	private static final Pattern NORTH_ = cleanWords("north");
	private static final String NORTH_REPLACEMENT = cleanWordsReplacement("N");

	private static final Pattern EAST_FR_ = cleanWordsFR("est");
	private static final String EAST_FR_REPLACEMENT = cleanWordsReplacement("E");
	private static final Pattern WEST_FR_ = cleanWordsFR("ouest");
	private static final String WEST_FR_REPLACEMENT = cleanWordsReplacement("O");
	private static final Pattern SOUTH_FR_ = cleanWordsFR("sud");
	private static final String SOUTH_FR_REPLACEMENT = cleanWordsReplacement("S");
	private static final Pattern NORTH_FR_ = cleanWordsFR("nord");
	private static final String NORTH_FR_REPLACEMENT = cleanWordsReplacement("N");

	@NotNull
	public static String cleanBounds(@NotNull String string) {
		return cleanBounds(Locale.ENGLISH, string);
	}

	@NotNull
	public static String cleanBounds(@NotNull Locale locale, @NotNull String string) {
		if (locale == Locale.FRENCH) {
			string = EAST_FR_.matcher(string).replaceAll(EAST_FR_REPLACEMENT);
			string = WEST_FR_.matcher(string).replaceAll(WEST_FR_REPLACEMENT);
			string = SOUTH_FR_.matcher(string).replaceAll(SOUTH_FR_REPLACEMENT);
			string = NORTH_FR_.matcher(string).replaceAll(NORTH_FR_REPLACEMENT);
		} else {
			string = EASTBOUND_.matcher(string).replaceAll(EASTBOUND_REPLACEMENT);
			string = WESTBOUND_.matcher(string).replaceAll(WESTBOUND_REPLACEMENT);
			string = SOUTHBOUND_.matcher(string).replaceAll(SOUTHBOUND_REPLACEMENT);
			string = NORTHBOUND_.matcher(string).replaceAll(NORTHBOUND_REPLACEMENT);
			string = EAST_.matcher(string).replaceAll(EAST_REPLACEMENT);
			string = WEST_.matcher(string).replaceAll(WEST_REPLACEMENT);
			string = SOUTH_.matcher(string).replaceAll(SOUTH_REPLACEMENT);
			string = NORTH_.matcher(string).replaceAll(NORTH_REPLACEMENT);
		}
		return string;
	}

	private static final Pattern ID_MERGED = Pattern.compile(
			group(group(atLeastOne(ALPHA_NUM_CAR)) + "_merged_" + group(atLeastOne(ALPHA_NUM_CAR)))
			, Pattern.CASE_INSENSITIVE);
	private static final String ID_MERGED_REPLACEMENT = mGroup(2);

	@NotNull
	public static String cleanMergedID(@NotNull String mergedId) {
		return ID_MERGED.matcher(mergedId).replaceAll(ID_MERGED_REPLACEMENT);
	}

	// http://www.semaphorecorp.com/cgi/abbrev.html
	private static final Pattern STREET = cleanWords("street");
	private static final String STREET_REPLACEMENT = cleanWordsReplacement("St");
	private static final Pattern AVENUE = cleanWords("avenue");
	private static final String AVENUE_REPLACEMENT = cleanWordsReplacement("Ave");
	private static final Pattern ROAD = cleanWords("road");
	private static final String ROAD_REPLACEMENT = cleanWordsReplacement("Rd");
	private static final Pattern HIGHWAY = cleanWords("highway");
	private static final String HIGHWAY_REPLACEMENT = cleanWordsReplacement("Hwy");
	private static final Pattern BOULEVARD = cleanWords("boulevard");
	private static final String BOULEVARD_REPLACEMENT = cleanWordsReplacement("Blvd");
	private static final Pattern DRIVE = cleanWords("drive");
	private static final String DRIVE_REPLACEMENT = cleanWordsReplacement("Dr");
	private static final Pattern PLACE = cleanWords("place");
	private static final String PLACE_REPLACEMENT = cleanWordsReplacement("Pl");
	private static final Pattern PLAZA = cleanWords("plaza");
	private static final String PLAZA_REPLACEMENT = cleanWordsReplacement("Plz");
	private static final Pattern LANE = cleanWords("lane");
	private static final String LANE_REPLACEMENT = cleanWordsReplacement("Ln");
	private static final Pattern CRESCENT = cleanWords("crescent");
	private static final String CRESCENT_REPLACEMENT = cleanWordsReplacement("Cr");
	private static final Pattern HEIGHTS = cleanWords("heights");
	private static final String HEIGHTS_REPLACEMENT = cleanWordsReplacement("Hts");
	private static final Pattern GROVE = cleanWords("grove");
	private static final String GROVE_REPLACEMENT = cleanWordsReplacement("Grv");

	public static final Pattern POINT = cleanWords("point");

	public static final String POINT_REPLACEMENT = cleanWordsReplacement("Pt");
	private static final Pattern POINTE = cleanWords("pointe");
	private static final String POINTE_REPLACEMENT = cleanWordsReplacement("Pte");
	private static final Pattern TERRACE = cleanWords("terrace");
	private static final String TERRACE_REPLACEMENT = cleanWordsReplacement("Ter");
	private static final Pattern MANOR = cleanWords("manor");
	private static final String MANOR_REPLACEMENT = cleanWordsReplacement("Mnr");
	private static final Pattern GREEN = cleanWords("green");
	private static final String GREEN_REPLACEMENT = cleanWordsReplacement("Grn");
	private static final Pattern VALLEY = cleanWords("valley", "vallley");
	private static final String VALLEY_REPLACEMENT = cleanWordsReplacement("Vly");
	private static final Pattern HILL = cleanWordsPlural("hill", "h ill");
	private static final String HILL_REPLACEMENT = cleanWordsReplacementPlural("Hl");
	private static final Pattern LAKE = cleanWordsPlural("lake");
	private static final String LAKE_REPLACEMENT = cleanWordsReplacementPlural("Lk");
	private static final Pattern MEADOW = cleanWordsPlural("meadow");
	private static final String MEADOW_REPLACEMENT = cleanWordsReplacementPlural("Mdw");
	private static final Pattern CIRCLE = cleanWords("circle");
	private static final String CIRCLE_REPLACEMENT = cleanWordsReplacement("Cir");
	private static final Pattern GLEN = cleanWords("glen");
	private static final String GLEN_REPLACEMENT = cleanWordsReplacement("Gln");
	private static final Pattern RIDGE = cleanWordsPlural("ridge");
	private static final String RIDGE_REPLACEMENT = cleanWordsReplacementPlural("Rdg");
	private static final Pattern GARDEN = cleanWordsPlural("garden");
	private static final String GARDEN_REPLACEMENT = cleanWordsReplacementPlural("Gdn");
	private static final Pattern CENTER = cleanWordsPlural("center", "centre", "ctre");
	private static final String CENTER_REPLACEMENT = cleanWordsReplacementPlural("Ctr");
	private static final Pattern ESTATE = cleanWordsPlural("estate");
	private static final String ESTATE_REPLACEMENT = cleanWordsReplacementPlural("Est");
	private static final Pattern LANDING = cleanWords("landing");
	private static final String LANDING_REPLACEMENT = cleanWordsReplacement("Lndg");
	private static final Pattern TRAIL = cleanWords("trail");
	private static final String TRAIL_REPLACEMENT = cleanWordsReplacement("Trl");
	private static final Pattern SPRING = cleanWordsPlural("spring");
	private static final String SPRING_REPLACEMENT = cleanWordsReplacementPlural("Spg");
	private static final Pattern VIEW = cleanWords("view");
	private static final String VIEW_REPLACEMENT = cleanWordsReplacement("Vw");
	private static final Pattern VILLAGE = cleanWords("village");
	private static final String VILLAGE_REPLACEMENT = cleanWordsReplacement("Vlg");
	private static final Pattern STATION = cleanWords("station");
	private static final String STATION_REPLACEMENT = cleanWordsReplacement("Sta");
	private static final Pattern RANCH = cleanWords("ranch");
	private static final String RANCH_REPLACEMENT = cleanWordsReplacement("Rnch");
	private static final Pattern COVE = cleanWords("cove");
	private static final String COVE_REPLACEMENT = cleanWordsReplacement("Cv");
	private static final Pattern SQUARE = cleanWords("square");
	private static final String SQUARE_REPLACEMENT = cleanWordsReplacement("Sq");
	private static final Pattern BROOK = cleanWords("brook");
	private static final String BROOK_REPLACEMENT = cleanWordsReplacement("Brk");
	private static final Pattern CREEK = cleanWords("creek");
	private static final String CREEK_REPLACEMENT = cleanWordsReplacement("Crk");
	private static final Pattern CROSSING = cleanWords("crossing");
	private static final String CROSSING_REPLACEMENT = cleanWordsReplacement("Xing");
	private static final Pattern CLIFF = cleanWordsPlural("cliff");
	private static final String CLIFF_REPLACEMENT = cleanWordsReplacementPlural("Clf");
	private static final Pattern SHORE = cleanWordsPlural("shore");
	private static final String SHORE_REPLACEMENT = cleanWordsReplacementPlural("Shr");
	private static final Pattern MOUNT = cleanWords("mount");
	private static final String MOUNT_REPLACEMENT = cleanWordsReplacement("Mt");
	private static final Pattern MOUNTAIN = cleanWords("mountain");
	private static final String MOUNTAIN_REPLACEMENT = cleanWordsReplacement("Mtn");
	private static final Pattern MARKET = cleanWords("market");
	private static final String MARKET_REPLACEMENT = cleanWordsReplacement("Mkt");
	private static final Pattern BUILDING = cleanWords("building");
	private static final String BUILDING_REPLACEMENT = cleanWordsReplacement("Bldg");
	private static final Pattern GREENS = cleanWords("greens");
	private static final String GREENS_REPLACEMENT = cleanWordsReplacement("Grns");
	private static final Pattern PARKWAY = cleanWords("parkway");
	private static final String PARKWAY_REPLACEMENT = cleanWordsReplacement("Pkwy");
	private static final Pattern ISLAND = cleanWordsPlural("island");
	private static final String ISLAND_REPLACEMENT = cleanWordsReplacementPlural("Isl");
	// https://public.oed.com/how-to-use-the-oed/abbreviations/
	private static final Pattern NEIGHBOURHOOD_ = cleanWordsPlural("neighbourhood", "neighbour");
	private static final String NEIGHBOURHOOD_REPLACEMENT = cleanWordsReplacementPlural("Neighb");
	// not official
	private static final Pattern APARTMENT_ = cleanWordsPlural("apartment");
	private static final String APARTMENT_REPLACEMENT = cleanWordsReplacementPlural("Apt"); // not official
	private static final Pattern BED_AND_BREAKFAST_ = cleanWords("bed and breakfast", "bed & breakfast", "b & b");
	private static final String BED_AND_BREAKFAST_REPLACEMENT = cleanWordsReplacement("B&B"); // not official
	private static final Pattern PARK_AND_RIDE_ = cleanWords("park and ride", "park & ride", "p & r", "park 'n' ride");
	private static final String PARK_AND_RIDE_REPLACEMENT = cleanWordsReplacement("P&R"); // not official
	private static final Pattern PARK = cleanWords("park");
	private static final String PARK_REPLACEMENT = cleanWordsReplacement("Pk"); // not official
	private static final Pattern EXCHANGE_ = cleanWords("exchange");
	private static final String EXCHANGE_REPLACEMENT = cleanWordsReplacement("Exch"); // not official
	private static final Pattern TERMINAL_ = cleanWords("terminal");
	private static final String TERMINAL_REPLACEMENT = cleanWordsReplacement("Term"); // not official
	private static final Pattern GATE = cleanWordsPlural("gate");
	private static final String GATE_REPLACEMENT = cleanWordsReplacementPlural("Gt"); // not official
	private static final Pattern DEPT_ = cleanWordsPlural("department");
	private static final String DEPT_REPLACEMENT = cleanWordsReplacementPlural("Dept"); // not official
	private static final Pattern PARKING = cleanWords("parking");
	private static final String PARKING_REPLACEMENT = cleanWordsReplacement("Pkng"); // not official
	private static final Pattern HOSPITAL = cleanWordsPlural("hospital");
	private static final String HOSPITAL_REPLACEMENT = cleanWordsReplacementPlural("Hosp"); // not official
	private static final Pattern GOVERNMENT_ = cleanWords("government");
	private static final String GOVERNMENT_REPLACEMENT = cleanWordsReplacement("Gov"); // not official
	private static final Pattern OPPOSITE_ = cleanWords("opposite");
	private static final String OPPOSITE_REPLACEMENT = cleanWordsReplacement("Opp"); // not official
	private static final Pattern OPERATION_ = cleanWordsPlural("operation");
	private static final String OPERATION_REPLACEMENT = cleanWordsReplacement("Op"); // not official
	private static final Pattern INDUSTRIAL_ = cleanWords("industrial");
	private static final String INDUSTRIAL_REPLACEMENT = cleanWordsReplacement("Ind"); // not official
	private static final Pattern COUNTER_CLOCKWISE_ = cleanWords("(counter( - |-|\\s)?clockwise)");
	private static final String COUNTER_CLOCKWISE_REPLACEMENT = cleanWordsReplacement("CCW"); // not official
	private static final Pattern CLOCKWISE_ = cleanWords("clockwise");
	private static final String CLOCKWISE_REPLACEMENT = cleanWordsReplacement("CW"); // not official
	//
	private static final Pattern BAY_ = Pattern.compile("((^|\\W)(bay #?(\\w))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String BAY_REPLACEMENT = "$2B:$4$5";
	private static final Pattern PLATFORM_ = Pattern.compile("((^|\\W)(platform #?(\\w{1,3}))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String PLATFORM_REPLACEMENT = "$2P:$4$5";
	private static final Pattern ELEMENTARY_SCHOOL_ = cleanWord("((\\w+) elementary school)");
	private static final String ELEMENTARY_SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 ES");
	private static final Pattern PRIMARY_SCHOOL_ = cleanWord("((\\w+) primary school)");
	private static final String PRIMARY_SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 PS");
	private static final Pattern SECONDARY_SCHOOL_ = cleanWord("((\\w+) secondary school)");
	private static final String SECONDARY_SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 SS");
	private static final Pattern MIDDLE_SCHOOL_ = cleanWord("((\\w+) middle school)");
	private static final String MIDDLE_SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 MS");
	private static final Pattern HIGH_SCHOOL_ = cleanWord("((\\w+) high school)");
	private static final String HIGH_SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 HS");
	private static final Pattern SCHOOL_ = cleanWord("((\\w+) school)");
	private static final String SCHOOL_REPLACEMENT = cleanWordsReplacement("$5 S");
	private static final Pattern UNIVERSITY_ = cleanWord("((\\w{4,}) university)"); // used for street names
	private static final String UNIVERSITY_REPLACEMENT = cleanWordsReplacement("$5 U");
	private static final Pattern UNIVERSITY_OF_ = cleanWord("(university of (\\w+))");
	private static final String UNIVERSITY_OF_REPLACEMENT = cleanWordsReplacement("U of $5");
	private static final Pattern COMMUNITY_CENTER_ = cleanWord("((\\w+) (community centre|community ctr))");
	private static final String COMMUNITY_CENTER_REPLACEMENT = cleanWordsReplacement("$5 CC");
	private static final Pattern TRANSIT_CENTER_ = cleanWord("((\\w+) (transit center|transit ctr))");
	private static final String TRANSIT_CENTER_REPLACEMENT = cleanWordsReplacement("$5 TC");
	private static final Pattern TRANSIT_TERM_ = cleanWord("((\\w+) (transit terminal|transit term))");
	private static final String TRANSIT_TERM_REPLACEMENT = cleanWordsReplacement("$5 TT");

	@NotNull
	public static String cleanStreetTypes(@NotNull String string) {
		string = LANE.matcher(string).replaceAll(LANE_REPLACEMENT);
		string = PLACE.matcher(string).replaceAll(PLACE_REPLACEMENT);
		string = PLAZA.matcher(string).replaceAll(PLAZA_REPLACEMENT);
		string = DRIVE.matcher(string).replaceAll(DRIVE_REPLACEMENT);
		string = BOULEVARD.matcher(string).replaceAll(BOULEVARD_REPLACEMENT);
		string = HIGHWAY.matcher(string).replaceAll(HIGHWAY_REPLACEMENT);
		string = STREET.matcher(string).replaceAll(STREET_REPLACEMENT);
		string = AVENUE.matcher(string).replaceAll(AVENUE_REPLACEMENT);
		string = ROAD.matcher(string).replaceAll(ROAD_REPLACEMENT);
		string = CRESCENT.matcher(string).replaceAll(CRESCENT_REPLACEMENT);
		string = HEIGHTS.matcher(string).replaceAll(HEIGHTS_REPLACEMENT);
		string = GROVE.matcher(string).replaceAll(GROVE_REPLACEMENT);
		string = POINT.matcher(string).replaceAll(POINT_REPLACEMENT);
		string = POINTE.matcher(string).replaceAll(POINTE_REPLACEMENT);
		string = TERRACE.matcher(string).replaceAll(TERRACE_REPLACEMENT);
		string = MANOR.matcher(string).replaceAll(MANOR_REPLACEMENT);
		string = GREEN.matcher(string).replaceAll(GREEN_REPLACEMENT);
		string = VALLEY.matcher(string).replaceAll(VALLEY_REPLACEMENT);
		string = LAKE.matcher(string).replaceAll(LAKE_REPLACEMENT);
		string = MEADOW.matcher(string).replaceAll(MEADOW_REPLACEMENT);
		string = CIRCLE.matcher(string).replaceAll(CIRCLE_REPLACEMENT);
		string = GLEN.matcher(string).replaceAll(GLEN_REPLACEMENT);
		string = RIDGE.matcher(string).replaceAll(RIDGE_REPLACEMENT);
		string = GARDEN.matcher(string).replaceAll(GARDEN_REPLACEMENT);
		string = CENTER.matcher(string).replaceAll(CENTER_REPLACEMENT);
		string = HILL.matcher(string).replaceAll(HILL_REPLACEMENT);
		string = ESTATE.matcher(string).replaceAll(ESTATE_REPLACEMENT);
		string = LANDING.matcher(string).replaceAll(LANDING_REPLACEMENT);
		string = TRAIL.matcher(string).replaceAll(TRAIL_REPLACEMENT);
		string = SPRING.matcher(string).replaceAll(SPRING_REPLACEMENT);
		string = VIEW.matcher(string).replaceAll(VIEW_REPLACEMENT);
		string = VILLAGE.matcher(string).replaceAll(VILLAGE_REPLACEMENT);
		string = STATION.matcher(string).replaceAll(STATION_REPLACEMENT);
		string = RANCH.matcher(string).replaceAll(RANCH_REPLACEMENT);
		string = COVE.matcher(string).replaceAll(COVE_REPLACEMENT);
		string = SQUARE.matcher(string).replaceAll(SQUARE_REPLACEMENT);
		string = BROOK.matcher(string).replaceAll(BROOK_REPLACEMENT);
		string = CREEK.matcher(string).replaceAll(CREEK_REPLACEMENT);
		string = CROSSING.matcher(string).replaceAll(CROSSING_REPLACEMENT);
		string = CLIFF.matcher(string).replaceAll(CLIFF_REPLACEMENT);
		string = SHORE.matcher(string).replaceAll(SHORE_REPLACEMENT);
		string = PARKING.matcher(string).replaceAll(PARKING_REPLACEMENT);
		string = MOUNT.matcher(string).replaceAll(MOUNT_REPLACEMENT);
		string = MOUNTAIN.matcher(string).replaceAll(MOUNTAIN_REPLACEMENT);
		//
		string = APARTMENT_.matcher(string).replaceAll(APARTMENT_REPLACEMENT);
		string = BED_AND_BREAKFAST_.matcher(string).replaceAll(BED_AND_BREAKFAST_REPLACEMENT);
		string = PARK_AND_RIDE_.matcher(string).replaceAll(PARK_AND_RIDE_REPLACEMENT);
		string = PARK.matcher(string).replaceAll(PARK_REPLACEMENT);
		string = NEIGHBOURHOOD_.matcher(string).replaceAll(NEIGHBOURHOOD_REPLACEMENT);
		string = EXCHANGE_.matcher(string).replaceAll(EXCHANGE_REPLACEMENT);
		string = TERMINAL_.matcher(string).replaceAll(TERMINAL_REPLACEMENT);
		string = DEPT_.matcher(string).replaceAll(DEPT_REPLACEMENT);
		string = GATE.matcher(string).replaceAll(GATE_REPLACEMENT);
		string = HOSPITAL.matcher(string).replaceAll(HOSPITAL_REPLACEMENT);
		string = GOVERNMENT_.matcher(string).replaceAll(GOVERNMENT_REPLACEMENT);
		string = MARKET.matcher(string).replaceAll(MARKET_REPLACEMENT);
		string = BUILDING.matcher(string).replaceAll(BUILDING_REPLACEMENT);
		string = GREENS.matcher(string).replaceAll(GREENS_REPLACEMENT);
		string = PARKWAY.matcher(string).replaceAll(PARKWAY_REPLACEMENT);
		string = ISLAND.matcher(string).replaceAll(ISLAND_REPLACEMENT);
		string = OPPOSITE_.matcher(string).replaceAll(OPPOSITE_REPLACEMENT);
		string = OPERATION_.matcher(string).replaceAll(OPERATION_REPLACEMENT);
		string = INDUSTRIAL_.matcher(string).replaceAll(INDUSTRIAL_REPLACEMENT);
		string = COUNTER_CLOCKWISE_.matcher(string).replaceAll(COUNTER_CLOCKWISE_REPLACEMENT); // before clockwise
		string = CLOCKWISE_.matcher(string).replaceAll(CLOCKWISE_REPLACEMENT);
		//
		string = BAY_.matcher(string).replaceAll(BAY_REPLACEMENT);
		string = PLATFORM_.matcher(string).replaceAll(PLATFORM_REPLACEMENT);
		string = ELEMENTARY_SCHOOL_.matcher(string).replaceAll(ELEMENTARY_SCHOOL_REPLACEMENT);
		string = PRIMARY_SCHOOL_.matcher(string).replaceAll(PRIMARY_SCHOOL_REPLACEMENT);
		string = SECONDARY_SCHOOL_.matcher(string).replaceAll(SECONDARY_SCHOOL_REPLACEMENT);
		string = MIDDLE_SCHOOL_.matcher(string).replaceAll(MIDDLE_SCHOOL_REPLACEMENT);
		string = HIGH_SCHOOL_.matcher(string).replaceAll(HIGH_SCHOOL_REPLACEMENT);
		string = SCHOOL_.matcher(string).replaceAll(SCHOOL_REPLACEMENT);
		string = UNIVERSITY_.matcher(string).replaceAll(UNIVERSITY_REPLACEMENT);
		string = UNIVERSITY_OF_.matcher(string).replaceAll(UNIVERSITY_OF_REPLACEMENT);
		string = COMMUNITY_CENTER_.matcher(string).replaceAll(COMMUNITY_CENTER_REPLACEMENT);
		string = TRANSIT_CENTER_.matcher(string).replaceAll(TRANSIT_CENTER_REPLACEMENT);
		string = TRANSIT_TERM_.matcher(string).replaceAll(TRANSIT_TERM_REPLACEMENT);
		return string;
	}

	// FR-CA : http://www.toponymie.gouv.qc.ca/ct/normes-procedures/terminologie-geographique/liste-termes-geographiques.html
	private static final Pattern FR_CA_AVENUE = cleanWordsPluralFR("avenue");
	private static final String FR_CA_AVENUE_REPLACEMENT = cleanWordsReplacementPlural("Av");
	private static final Pattern FR_CA_AUTOROUTE = cleanWordsFR("autoroute");
	private static final String FR_CA_AUTOROUTE_REPLACEMENT = cleanWordsReplacement("Aut");
	private static final Pattern FR_CA_BOULEVARD = cleanWordsFR("boulevard");
	private static final String FR_CA_BOULEVARD_REPLACEMENT = cleanWordsReplacement("Boul");
	private static final Pattern FR_CA_CARREFOUR = cleanWordsFR("carrefour");
	private static final String FR_CA_CARREFOUR_REPLACEMENT = cleanWordsReplacement("Carref");
	private static final Pattern FR_CA_MONTAGNE = cleanWordsFR("montagne");
	private static final String FR_CA_MONTAGNE_REPLACEMENT = cleanWordsReplacement("Mgne");
	private static final Pattern FR_CA_MONTEE = cleanWordsFR("mont[é|e]e");
	private static final String FR_CA_MONTEE_REPLACEMENT = cleanWordsReplacement("Mtée");
	private static final Pattern FR_CA_PARC_INDUSTRIEL = cleanWordsFR("parc industriel");
	private static final String FR_CA_PARC_INDUSTRIEL_REPLACEMENT = cleanWordsReplacement("Parc Ind");
	private static final Pattern FR_CA_RIVIERE = cleanWordsFR("rivi[e|è]re");
	private static final String FR_CA_RIVIERE_REPLACEMENT = cleanWordsReplacement("Riv");
	private static final Pattern FR_CA_SECTEURS_ = cleanWordsPluralFR("secteur");
	private static final String FR_CA_SECTEURS_REPLACEMENT = cleanWordsReplacementPlural("Sect");
	private static final Pattern FR_CA_STATION_DE_METRO = cleanWordsFR("Station de m[é|e]tro");
	private static final String FR_CA_STATION_DE_METRO_REPLACEMENT = cleanWordsReplacement("Ston mét");
	private static final Pattern FR_CA_STATION = cleanWordsFR("station");
	private static final String FR_CA_STATION_REPLACEMENT = cleanWordsReplacement("Ston");
	private static final Pattern FR_CA_STATIONNEMENT = cleanWordsFR("stationnement");
	private static final String FR_CA_STATIONNEMENT_REPLACEMENT = cleanWordsReplacement("Stat");
	private static final Pattern FR_CA_TERRASSE = cleanWordsFR("terrasse");
	private static final String FR_CA_TERRASSE_REPLACEMENT = cleanWordsReplacement("Tsse");
	private static final Pattern FR_CA_TERRASSES = cleanWordsFR("terrasses");
	private static final String FR_CA_TERRASSES_REPLACEMENT = cleanWordsReplacement("Tsses");
	private static final Pattern FR_CA_POINTE = cleanWordsFR("pointe");
	private static final String FR_CA_POINTE_REPLACEMENT = cleanWordsReplacement("Pte");
	private static final Pattern FR_CA_PLACE = cleanWordsFR("place");
	private static final String FR_CA_PLACE_REPLACEMENT = cleanWordsReplacement("Pl");
	// not official
	private static final Pattern FR_CA_POINT = cleanWordsFR("point");
	private static final String FR_CA_POINT_REPLACEMENT = cleanWordsReplacement("Pt");
	private static final Pattern FR_CA_CENTRE_ = cleanWordsPluralFR("centre");
	private static final String FR_CA_CENTRE_REPLACEMENT = cleanWordsReplacementPlural("Ctr");
	private static final Pattern FR_CA_TERMINUS = cleanWordsFR("terminus");
	private static final String FR_CA_TERMINUS_REPLACEMENT = cleanWordsReplacement("Term");
	private static final Pattern FR_CA_TEMPORAIRE = cleanWordsFR("temporaire");
	private static final String FR_CA_TEMPORAIRE_REPLACEMENT = cleanWordsReplacement("Temp");
	private static final Pattern FR_CA_PRO_ = cleanWordsFR("professionnelle", "professionnel", "professionelle", "professionel");
	private static final String FR_CA_PRO_REPLACEMENT = cleanWordsReplacement("Pro");
	private static final Pattern FR_CA_INFO_ = cleanWordsPluralFR("information");
	private static final String FR_CA_INFO_REPLACEMENT = cleanWordsReplacementPlural("Into");
	private static final Pattern FR_CA_INDUSTRIEL_ = cleanWordsPluralFR("industriel");
	private static final String FR_CA_INDUSTRIEL_REPLACEMENT = cleanWordsReplacementPlural("Ind");
	private static final Pattern FR_CA_PARC_RELAIS_ = cleanWordsFR("parc relais", "stationnement incitatif", "stat incitatif");
	private static final String FR_CA_PARC_RELAIS_REPLACEMENT = cleanWordsReplacement("P+R");
	//
	private static final Pattern FR_CA_QUAI_ = Pattern.compile("((^|\\W)(quai( #?|: )(\\w{1,4}))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String FR_CA_QUAI_REPLACEMENT = "$2Q:$5$6";
	private static final Pattern FR_CA_PORTE_ = Pattern.compile("((^|\\W)(porte #?(\\w{1,4}))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String FR_CA_PORTE_REPLACEMENT = "$2P:$4$5";
	private static final Pattern FR_CA_ECOLE_SECONDAIRE_ = cleanWordFR("((école secondaire|ecole secondaire|école sec|ecole sec)(\\.|\\s){1,2}(\\w+))");
	private static final String FR_CA_ECOLE_SECONDAIRE_REPLACEMENT = cleanWordsReplacement("ÉS $7");
	private static final Pattern FR_CA_UNIVERSITE_ = cleanWordFR("((université|univ|u)(\\.|\\s){1,2}(\\w+))");
	private static final String FR_CA_UNIVERSITE_REPLACEMENT = cleanWordsReplacement("U $7");

	@NotNull
	public static String cleanStreetTypesFRCA(@NotNull String string) {
		string = FR_CA_AVENUE.matcher(string).replaceAll(FR_CA_AVENUE_REPLACEMENT);
		string = FR_CA_AUTOROUTE.matcher(string).replaceAll(FR_CA_AUTOROUTE_REPLACEMENT);
		string = FR_CA_BOULEVARD.matcher(string).replaceAll(FR_CA_BOULEVARD_REPLACEMENT);
		string = FR_CA_CARREFOUR.matcher(string).replaceAll(FR_CA_CARREFOUR_REPLACEMENT);
		string = FR_CA_MONTAGNE.matcher(string).replaceAll(FR_CA_MONTAGNE_REPLACEMENT);
		string = FR_CA_MONTEE.matcher(string).replaceAll(FR_CA_MONTEE_REPLACEMENT);
		string = FR_CA_PARC_INDUSTRIEL.matcher(string).replaceAll(FR_CA_PARC_INDUSTRIEL_REPLACEMENT);
		string = FR_CA_RIVIERE.matcher(string).replaceAll(FR_CA_RIVIERE_REPLACEMENT);
		string = FR_CA_SECTEURS_.matcher(string).replaceAll(FR_CA_SECTEURS_REPLACEMENT);
		string = FR_CA_STATION_DE_METRO.matcher(string).replaceAll(FR_CA_STATION_DE_METRO_REPLACEMENT);
		string = FR_CA_STATION.matcher(string).replaceAll(FR_CA_STATION_REPLACEMENT);
		string = FR_CA_STATIONNEMENT.matcher(string).replaceAll(FR_CA_STATIONNEMENT_REPLACEMENT);
		string = FR_CA_TERRASSE.matcher(string).replaceAll(FR_CA_TERRASSE_REPLACEMENT);
		string = FR_CA_TERRASSES.matcher(string).replaceAll(FR_CA_TERRASSES_REPLACEMENT);
		string = FR_CA_POINTE.matcher(string).replaceAll(FR_CA_POINTE_REPLACEMENT);
		string = FR_CA_PLACE.matcher(string).replaceAll(FR_CA_PLACE_REPLACEMENT);
		// not official
		string = FR_CA_CENTRE_.matcher(string).replaceAll(FR_CA_CENTRE_REPLACEMENT);
		string = FR_CA_TERMINUS.matcher(string).replaceAll(FR_CA_TERMINUS_REPLACEMENT);
		string = FR_CA_PRO_.matcher(string).replaceAll(FR_CA_PRO_REPLACEMENT);
		string = FR_CA_INFO_.matcher(string).replaceAll(FR_CA_INFO_REPLACEMENT);
		string = FR_CA_INDUSTRIEL_.matcher(string).replaceAll(FR_CA_INDUSTRIEL_REPLACEMENT);
		string = FR_CA_PARC_RELAIS_.matcher(string).replaceAll(FR_CA_PARC_RELAIS_REPLACEMENT);
		string = FR_CA_TEMPORAIRE.matcher(string).replaceAll(FR_CA_TEMPORAIRE_REPLACEMENT);
		//
		string = FR_CA_QUAI_.matcher(string).replaceAll(FR_CA_QUAI_REPLACEMENT);
		string = FR_CA_PORTE_.matcher(string).replaceAll(FR_CA_PORTE_REPLACEMENT);
		string = FR_CA_ECOLE_SECONDAIRE_.matcher(string).replaceAll(FR_CA_ECOLE_SECONDAIRE_REPLACEMENT);
		string = FR_CA_UNIVERSITE_.matcher(string).replaceAll(FR_CA_UNIVERSITE_REPLACEMENT);
		return string;
	}
}
