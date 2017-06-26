package ff.projects.utils;

import java.util.Date;

/**
 * Created by F on 2017/6/24.
 */
public class TimeHelper {
    private final static long DIFF_UNIX_WIN_MS = 11644473600000L;
    private final static long MULTIPLE_MS_100NS = 10000;

    private TimeHelper() {

    }

    public static Date Filetime2Date(long llt) {
        llt /= MULTIPLE_MS_100NS;
        llt -= DIFF_UNIX_WIN_MS;
        return new Date(llt);
    }
    public static long Date2Filetime(Date d) {
        long llt;

        llt = d.getTime();
        llt += DIFF_UNIX_WIN_MS;
        llt *= MULTIPLE_MS_100NS;
        return llt;
    }
}
