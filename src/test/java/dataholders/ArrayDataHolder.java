package dataholders;

import java.util.ArrayList;

public class ArrayDataHolder {

  ArrayList<String> arrayList = new ArrayList<>(){{
    add("test string");
  }};

  public ArrayList<String> getArrayList() {
    return arrayList;
  }

  public void setArrayList(ArrayList<String> arrayList) {
    this.arrayList = arrayList;
  }

}
