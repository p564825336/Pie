package com.exam.pie;

public class RotatedValueUtil {

	public static int getRotatedValue(int paramInt1, int paramInt2, int[] paramArrayOfInt)
	  {
	    int i = paramInt1 % paramArrayOfInt.length;
	    if ((paramInt1 == paramInt2) && (i == 0))
	      i = 1;
	    return paramArrayOfInt[i];
	  }
}
