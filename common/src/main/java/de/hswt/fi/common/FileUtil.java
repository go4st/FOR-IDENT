package de.hswt.fi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by August Gilg on 19.05.2017.
 */
public final class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static final String LOG_MESSAGE_TRY_ENCODE = "Trying to encode file with charset {}";

	private static final String LOG_MESSAGE_SUCCESS_ENCODE = "Successfully decoded file with charset {}";

	private static final String LOG_MESSAGE_ERROR_ENCODE = "Could not encode file with charset {}";

    private static final List<Charset> CHARSETS = Arrays.asList(StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII);

    private FileUtil() {
        throw new IllegalStateException("Static utility class should not be initialized");
    }

    public static String readFirstLine(Path path) {
        for (Charset charset : CHARSETS) {

            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                LOGGER.debug(LOG_MESSAGE_TRY_ENCODE, charset);
                String line = reader.readLine();
                LOGGER.debug(LOG_MESSAGE_SUCCESS_ENCODE, charset);
                return line;
            } catch (MalformedInputException e) {
                LOGGER.error(LOG_MESSAGE_ERROR_ENCODE, charset);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return "";
    }

    public static List<String> readLines(Path path, int amount) {

        List<String> lines = new ArrayList<>();

        for (Charset charset : CHARSETS) {
            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                LOGGER.debug(LOG_MESSAGE_SUCCESS_ENCODE, charset);
                for (int i = 0; i < amount; i++) {
                    lines.add(reader.readLine());
                }
                LOGGER.debug(LOG_MESSAGE_TRY_ENCODE, charset);
                return lines;
            } catch (MalformedInputException e) {
                LOGGER.error(LOG_MESSAGE_ERROR_ENCODE, charset);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return Collections.emptyList();
    }

    public static List<String> readAllLines(Path path) {

        for (Charset charset : CHARSETS) {
            try {
                LOGGER.debug(LOG_MESSAGE_TRY_ENCODE, charset);
                List<String> lines = Files.readAllLines(path, charset);
                LOGGER.debug(LOG_MESSAGE_SUCCESS_ENCODE, charset);
                return lines;
            } catch (MalformedInputException e) {
                LOGGER.error(LOG_MESSAGE_ERROR_ENCODE, charset);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return Collections.emptyList();
    }
}
