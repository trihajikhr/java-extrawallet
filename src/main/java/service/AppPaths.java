package service;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {

    public static final boolean IS_DEV;
    public static final Path ROOT;
    public static final Path DATABASE_DIR;
    public static final Path DB_FILE;
    public static final Path EXCHANGE_JSON;

    static {
        IS_DEV = "dev".equalsIgnoreCase(
                System.getProperty("app.mode", "prod")
        );

        ROOT = IS_DEV
                ? Paths.get("data")
                : Paths.get(System.getProperty("user.home"), ".extrawallet");

        DATABASE_DIR = ROOT.resolve("database");
        DB_FILE = DATABASE_DIR.resolve("finance.db");

        EXCHANGE_JSON = ROOT
                .resolve("currency-exchange")
                .resolve("exchange_rates.json");
    }

    private AppPaths() {}
}
