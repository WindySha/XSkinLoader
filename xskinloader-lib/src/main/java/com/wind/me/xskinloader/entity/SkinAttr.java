package com.wind.me.xskinloader.entity;

public class SkinAttr {
    /***
     * 对应View的属性
     */
    public String attrName;

    /***
     * 属性值对应的reference id值，类似R.color.XX
     */
    public int attrValueRefId;

    /***
     * 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
     */
    public String attrValueRefName;

    /***
     * 属性值refrence id对应的类型，如R.color.XX，则此值为color
     */
    public String attrValueTypeName;

    public SkinAttr(String attrName, int attrValueRefId, String attrValueRefName, String attrValueTypeName) {
        this.attrName = attrName;
        this.attrValueRefId = attrValueRefId;
        this.attrValueRefName = attrValueRefName;
        this.attrValueTypeName = attrValueTypeName;
    }

    @Override
    public String toString() {
        return "SkinAttr \n[\nattrName=" + attrName + ", \n"
                + "attrValueRefId=" + attrValueRefId + ", \n"
                + "attrValueRefName=" + attrValueRefName + ", \n"
                + "attrValueTypeName=" + attrValueTypeName
                + "\n]";
    }
}
