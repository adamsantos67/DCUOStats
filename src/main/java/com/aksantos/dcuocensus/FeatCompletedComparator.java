package com.aksantos.dcuocensus;

import java.util.Comparator;

import com.aksantos.dcuocensus.models.Feat;

public class FeatCompletedComparator implements Comparator<Feat> {
    private static final FeatCompletedComparator INSTANCE = new FeatCompletedComparator();

    private FeatCompletedComparator() {
    }

    public static FeatCompletedComparator getInstance() {
        return INSTANCE;
    }

    public int compare(Feat feat1, Feat feat2) {
        int ret = (int) (feat2.getCompleted() - feat1.getCompleted());
        if (ret == 0) {
            ret = feat1.getOrder1() - feat2.getOrder1();

            if (ret == 0) {
                ret = feat1.getOrder2() - feat2.getOrder2();
                if (ret == 0) {
                    ret = feat1.getCategory().compareTo(feat2.getCategory());
                    if (ret == 0) {
                        ret = feat1.getSubCategory().compareTo(feat2.getSubCategory());
                        if (ret == 0) {
                            ret = feat1.getNameEn().compareTo(feat2.getNameEn());
                            if (ret == 0) {
                                ret = (int) (feat1.getId() - feat2.getId());
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

}
