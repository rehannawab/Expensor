import com.fiosys.expensor.category.Category;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

/**
 * Created by root on 21/9/15.
 */
public class ExpensorDatabaseConfigUtil extends OrmLiteConfigUtil {

//    private static final Class<?>[] classes = new Class[]{
//            Category.class,
//    };

    public static void main(String[] args) throws Exception {
//        writeConfigFile("/home/rehan/AndroidStudioProjects/Expensor/app/src/main/res/raw/ormlite_config.txt", classes);
        writeConfigFile(new File("/home/rehan/AndroidStudioProjects/Expensor/app/src/main/res/raw/ormlite_config.txt"), new File("/home/rehan/AndroidStudioProjects/Expensor/app/src/main/java"));
    }

}
