package org.openmarkov.io.odata;

import java.util.List;

public abstract class ValueElementDiscretization {
	
	public class ValueVariationDiscretization extends ValueElementDiscretization{
		private double variationRate;
		@Override
		public boolean getValue(List<Double> rawValues) {
			double min= Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (double rawValue : rawValues) {
				if (rawValue < min) min = rawValue;
				if (rawValue > min) max = rawValue;
			}
			boolean value = ((max - min )/max) > variationRate;
			return value;
		}	
	}
	
	
	public enum DiscretizationMethod {
		VALUE_VARIATION,
		LOGICAL_OP,
		CATEGORIES
	}

	public static DiscretizationMethod[] getDiscretizationMethodsName(){
		return DiscretizationMethod.values();
	}

	public static ValueElementDiscretization getValueVariationMethod(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	abstract public boolean getValue(List<Double> rawValues);
	
}
