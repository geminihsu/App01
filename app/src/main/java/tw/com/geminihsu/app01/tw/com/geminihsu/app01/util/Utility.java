package tw.com.geminihsu.app01.tw.com.geminihsu.app01.util;

import android.widget.EditText;

/**
 * Created by Shreya Kotak on 04/05/16.
 */
public class Utility
{
    public static boolean isBlankField(EditText etPersonData)
    {
        return etPersonData.getText().toString().trim().equals("");
    }
}
