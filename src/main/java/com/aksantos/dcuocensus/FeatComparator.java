package com.aksantos.dcuocensus;

import java.util.Comparator;

import com.aksantos.dcuocensus.models.Feat;

public class FeatComparator implements Comparator<Feat> {
    private static final FeatComparator INSTANCE = new FeatComparator();

    private FeatComparator() {
    }

    public static FeatComparator getInstance() {
        return INSTANCE;
    }

    public int compare(Feat feat1, Feat feat2) {
        int ret = feat1.getOrder1() - feat2.getOrder1();

        if (ret == 0) {
            ret = feat1.getOrder2() - feat2.getOrder2();
            if (ret == 0) {
                ret = feat1.getCategory().compareTo(feat2.getCategory());
                if (ret == 0) {
                    ret = feat1.getSubCategory().compareTo(feat2.getSubCategory());
                    if (ret == 0) {
                        if (feat1.getOrder1() == 0 && feat1.getOrder2() == 0) {
                            ret = feat1.getNameEn().compareTo(feat2.getNameEn());
                            if (ret == 0) {
                                ret = (int) (feat1.getId() - feat2.getId());
                            }
                        } else {
                            ret = (int) (feat1.getId() - feat2.getId());
                        }
                    }
                }
            }
        }
        return ret;
    }

}
