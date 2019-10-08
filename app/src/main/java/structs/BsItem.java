package structs;

public class BsItem {
    private String bsType;
    private int backGroundImage;

    public BsItem(String bsType, int backGroundImage) {
        this.bsType = bsType;
        this.backGroundImage = backGroundImage;
    }

    public String getBsType() {
        return bsType;
    }

    public int getBackGroundImage() {
        return backGroundImage;
    }
}
