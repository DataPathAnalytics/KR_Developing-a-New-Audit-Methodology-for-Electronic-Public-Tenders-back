package com.datapath.kg.risks.loader.utils;

public enum Indicator {

    KRAI1(1),
    KRAI2(2),
    KRAI3(3),
    KRAI5(5),
    KRAI8(8),
    KRAI12(12),
    KRAI13(13),
    KRAI14(14),
    KRAI18(18),
    KRAI19(19),
    KRAI21(21),
    KRAI22(22),
    KRAI29(29),
    KRAI30(30),
    KRAI31(31),
    KRAI32(32),
    ;

    private Integer id;

    Indicator(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
