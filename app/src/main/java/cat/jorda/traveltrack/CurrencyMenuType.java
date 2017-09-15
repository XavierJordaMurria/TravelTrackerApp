package cat.jorda.traveltrack;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xj1 on 15/09/2017.
 */

public final class CurrencyMenuType
{
    public static final int FROM_CURRENCY   = 8200;
    public static final int TO_CURRENCY     = 8201;

    @IntDef({FROM_CURRENCY, TO_CURRENCY})
    @Retention(RetentionPolicy.SOURCE)

    public @interface ICurrencyMenuType {}
}