package com.intel.tvpresent;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class AndroidTvApplication extends TinkerApplication {

    public AndroidTvApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.intel.tvpresent.AndroidTvApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
