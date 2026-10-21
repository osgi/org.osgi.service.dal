/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/

package org.osgi.test.cases.dal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.service.dal.SIUnits;

/**
 * Validates the values of {@link SIUnits} constants.
 */
public final class SIUnitsTest {

	/**
	 * Tests SI base unit symbols.
	 */
	@Test
	public void testSIBaseUnits() {
		assertThat(SIUnits.METER).as("Meter symbol is not correct.").isEqualTo("m");
		assertThat(SIUnits.KILOGRAM).as("Kilogram symbol is not correct.").isEqualTo("kg");
		assertThat(SIUnits.SECOND).as("Second symbol is not correct.").isEqualTo("s");
		assertThat(SIUnits.AMPERE).as("Ampere symbol is not correct.").isEqualTo("A");
		assertThat(SIUnits.KELVIN).as("Kelvin symbol is not correct.").isEqualTo("\u212A");
		assertThat(SIUnits.MOLE).as("Mole symbol is not correct.").isEqualTo("mol");
		assertThat(SIUnits.CANDELA).as("Candela symbol is not correct.").isEqualTo("cd");
	}

	/**
	 * Tests derived units in the SI expressed in terms of base units.
	 */
	@Test
	public void testSIDerivedUnits() {
		assertThat(SIUnits.SQUARE_METER).as("Square meter symbol is not correct.").isEqualTo(SIUnits.METER + "\u00B2");
		assertThat(SIUnits.CUBIC_METER).as("Cubic meter symbol is not correct.").isEqualTo(SIUnits.METER + "\u00B3");
		assertThat(SIUnits.METER_PER_SECOND).as("Meter per second symbol is not correct.").isEqualTo(SIUnits.METER + '/' + SIUnits.SECOND);
		assertThat(SIUnits.METER_PER_SECOND_SQUARED).as("Meter per second squared symbol is not correct.").isEqualTo(SIUnits.METER + '/' + SIUnits.SECOND + "\u00B2");
		assertThat(SIUnits.RECIPROCAL_METER).as("Reciprocal meter symbol is not correct.").isEqualTo(SIUnits.METER + "\u207B\u00B9");
		assertThat(SIUnits.KILOGRAM_PER_CUBIC_METER).as("Kilogram per cubic meter symbol is not correct.").isEqualTo(SIUnits.KILOGRAM + '/' + SIUnits.CUBIC_METER);
		assertThat(SIUnits.KILOGRAM_PER_SQUARE_METER).as("Kilogram per square meter symbol is not correct.").isEqualTo(SIUnits.KILOGRAM + '/' + SIUnits.SQUARE_METER);
		assertThat(SIUnits.CUBIC_METER_PER_KILOGRAM).as("Cubic meter per kilogram symbol is not correct.").isEqualTo(SIUnits.CUBIC_METER + '/' + SIUnits.KILOGRAM);
		assertThat(SIUnits.AMPERE_PER_SQUARE_METER).as("Ampere per square meter symbol is not correct.").isEqualTo(SIUnits.AMPERE + '/' + SIUnits.SQUARE_METER);
		assertThat(SIUnits.AMPERE_PER_METER).as("Ampere per meter symbol is not correct.").isEqualTo(SIUnits.AMPERE + '/' + SIUnits.METER);
		assertThat(SIUnits.MOLE_PER_CUBIC_METER).as("Mole per cubic meter symbol is not correct.").isEqualTo(SIUnits.MOLE + '/' + SIUnits.CUBIC_METER);
		assertThat(SIUnits.CANDELA_PER_SQUARE_METER).as("Candela per square meter symbol is not correct.").isEqualTo(SIUnits.CANDELA + '/' + SIUnits.SQUARE_METER);
	}

	/**
	 * Tests derived units in the SI with special names and symbols.
	 */
	@Test
	public void testSISpecialDerivedUnits() {
		assertThat(SIUnits.RADIAN).as("Radian symbol is not correct.").isEqualTo("rad");
		assertThat(SIUnits.STERADIAN).as("Steradian symbol is not correct.").isEqualTo("sr");
		assertThat(SIUnits.HERTZ).as("Hertz symbol is not correct.").isEqualTo("Hz");
		assertThat(SIUnits.NEWTON).as("Newton symbol is not correct.").isEqualTo("N");
		assertThat(SIUnits.PASCAL).as("Pascal symbol is not correct.").isEqualTo("Pa");
		assertThat(SIUnits.JOULE).as("Joule symbol is not correct.").isEqualTo("J");
		assertThat(SIUnits.WATT).as("Watt symbol is not correct.").isEqualTo("W");
		assertThat(SIUnits.COULOMB).as("Coulomb symbol is not correct.").isEqualTo("C");
		assertThat(SIUnits.VOLT).as("Volt symbol is not correct.").isEqualTo("V");
		assertThat(SIUnits.FARAD).as("Farad symbol is not correct.").isEqualTo("F");
		assertThat(SIUnits.OHM).as("Ohm symbol is not correct.").isEqualTo("\u2126");
		assertThat(SIUnits.SIEMENS).as("Siemens symbol is not correct.").isEqualTo("S");
		assertThat(SIUnits.WEBER).as("Weber symbol is not correct.").isEqualTo("Wb");
		assertThat(SIUnits.TESLA).as("Tesla symbol is not correct.").isEqualTo("T");
		assertThat(SIUnits.HENRY).as("Henry symbol is not correct.").isEqualTo("H");
		assertThat(SIUnits.DEGREE_CELSIUS).as("Degree Celsius symbol is not correct.").isEqualTo("\u2103");
		assertThat(SIUnits.LUMEN).as("Lumen symbol is not correct.").isEqualTo("lm");
		assertThat(SIUnits.LUX).as("Lux symbol is not correct.").isEqualTo("lx");
		assertThat(SIUnits.BECQUEREL).as("Becquerel symbol is not correct.").isEqualTo("Bq");
		assertThat(SIUnits.GRAY).as("Gray symbol is not correct.").isEqualTo("Gy");
		assertThat(SIUnits.SIEVERT).as("Sievert symbol is not correct.").isEqualTo("Sv");
		assertThat(SIUnits.KATAL).as("Katal symbol is not correct.").isEqualTo("kat");
	}

	/**
	 * Tests SI coherent derived units whose names and symbols include SI
	 * coherent derived units with special names and symbols.
	 */
	@Test
	public void testSICompositeSpecialDerivedUnits() {
		assertThat(SIUnits.PASCAL_SECOND).as("Pascal second symbol is not correct.").isEqualTo(SIUnits.PASCAL + ' ' + SIUnits.SECOND);
		assertThat(SIUnits.NEWTON_METER).as("Newton meter symbol is not correct.").isEqualTo(SIUnits.NEWTON + ' ' + SIUnits.METER);
		assertThat(SIUnits.NEWTON_PER_METER).as("Newton per meter symbol is not correct.").isEqualTo(SIUnits.NEWTON + '/' + SIUnits.METER);
		assertThat(SIUnits.RADIAN_PER_SECOND).as("Radian per second symbol is not correct.").isEqualTo(SIUnits.RADIAN + '/' + SIUnits.SECOND);
		assertThat(SIUnits.RADIAN_PER_SECOND_SQUARED).as("Radian per second squared symbol is not correct.").isEqualTo(SIUnits.RADIAN + '/' + SIUnits.SECOND + "\u00B2");
		assertThat(SIUnits.WATT_PER_SQUARE_METER).as("Watt per square meter symbol is not correct.").isEqualTo(SIUnits.WATT + '/' + SIUnits.METER + "\u00B2");
		assertThat(SIUnits.JOULE_PER_KELVIN).as("Joule per kelvin symbol is not correct.").isEqualTo(SIUnits.JOULE + '/' + SIUnits.KELVIN);
		assertThat(SIUnits.JOULE_PER_KILOGRAM_KELVIN).as("Joule per kilogram kelvin symbol is not correct.").isEqualTo(SIUnits.JOULE + "/(" + SIUnits.KILOGRAM + ' ' + SIUnits.KELVIN + ')');
		assertThat(SIUnits.JOULE_PER_KILOGRAM).as("Joule per kilogram symbol is not correct.").isEqualTo(SIUnits.JOULE + '/' + SIUnits.KILOGRAM);
		assertThat(SIUnits.WATT_PER_METER_KELVIN).as("Watt per meter kelvin symbol is not correct.").isEqualTo(SIUnits.WATT + "/(" + SIUnits.METER + ' ' + SIUnits.KELVIN + ')');
		assertThat(SIUnits.VOLT_PER_METER).as("Volt per meter symbol is not correct.").isEqualTo(SIUnits.VOLT + '/' + SIUnits.METER);
		assertThat(SIUnits.COULOMB_PER_CUBIC_METER).as("Coulomb per cubic meter symbol is not correct.").isEqualTo(SIUnits.COULOMB + '/' + SIUnits.METER + "\u00B3");
		assertThat(SIUnits.COULOMB_PER_SQUARE_METER).as("Coulomb per square meter symbol is not correct.").isEqualTo(SIUnits.COULOMB + '/' + SIUnits.METER + "\u00B2");
		assertThat(SIUnits.FARAD_PER_METER).as("Farad per meter symbol is not correct.").isEqualTo(SIUnits.FARAD + '/' + SIUnits.METER);
		assertThat(SIUnits.HENRY_PER_METER).as("Henry per meter symbol is not correct.").isEqualTo(SIUnits.HENRY + '/' + SIUnits.METER);
		assertThat(SIUnits.JOULE_PER_MOLE).as("Joule per mole symbol is not correct.").isEqualTo(SIUnits.JOULE + '/' + SIUnits.MOLE);
		assertThat(SIUnits.JOULE_PER_MOLE_KELVIN).as("Joule per mole kelvin symbol is not correct.").isEqualTo(SIUnits.JOULE + "/(" + SIUnits.MOLE + ' ' + SIUnits.KELVIN + ')');
		assertThat(SIUnits.COULOMB_PER_KILOGRAM).as("Coulomb per kilogram symbol is not correct.").isEqualTo(SIUnits.COULOMB + '/' + SIUnits.KILOGRAM);
		assertThat(SIUnits.GRAY_PER_SECOND).as("Gray per second symbol is not correct.").isEqualTo(SIUnits.GRAY + '/' + SIUnits.SECOND);
		assertThat(SIUnits.WATT_PER_STERADIAN).as("Watt per steradian symbol is not correct.").isEqualTo(SIUnits.WATT + '/' + SIUnits.STERADIAN);
		assertThat(SIUnits.WATT_PER_SQUARE_METER_STERADIAN).as("Watt per square meter steradian symbol is not correct.").isEqualTo(SIUnits.WATT + "/(" + SIUnits.METER + "\u00B2 " + SIUnits.STERADIAN + ')');
		assertThat(SIUnits.KATAL_PER_CUBIC_METER).as("Katal per cubic meter symbol is not correct.").isEqualTo(SIUnits.KATAL + '/' + SIUnits.METER + "\u00B3");
	}

	/**
	 * Tests non-SI units accepted for use with the International System of
	 * Units.
	 */
	@Test
	public void testNonSIUnits() {
		assertThat(SIUnits.TIME_MINUTE).as("Time minute symbol is not correct.").isEqualTo("min");
		assertThat(SIUnits.HOUR).as("Hour symbol is not correct.").isEqualTo("h");
		assertThat(SIUnits.DAY).as("Day symbol is not correct.").isEqualTo("d");
		assertThat(SIUnits.DEGREE).as("Degree symbol is not correct.").isEqualTo("\u00B0");
		assertThat(SIUnits.PLANE_ANGLE_MINUTE).as("Plane angle minute symbol is not correct.").isEqualTo("\u2032");
		assertThat(SIUnits.PLANE_ANGLE_SECOND).as("Plane angle second symbol is not correct.").isEqualTo("\u2033");
		assertThat(SIUnits.HECTARE).as("Hectare symbol is not correct.").isEqualTo("ha");
		assertThat(SIUnits.LITER).as("Liter symbol is not correct.").isEqualTo("l");
		assertThat(SIUnits.TONNE).as("Tonne symbol is not correct.").isEqualTo("t");
		assertThat(SIUnits.BAR).as("Bar symbol is not correct.").isEqualTo("bar");
		assertThat(SIUnits.MILLIMETER_OF_MERCURY).as("Millimeter of mercury symbol is not correct.").isEqualTo("mmHg");
		assertThat(SIUnits.ANGSTROM).as("Angstrom symbol is not correct.").isEqualTo("\u212B");
		assertThat(SIUnits.NAUTICAL_MILE).as("Nautical mile symbol is not correct.").isEqualTo("M");
		assertThat(SIUnits.BARN).as("Barn symbol is not correct.").isEqualTo("b");
		assertThat(SIUnits.KNOT).as("Knot symbol is not correct.").isEqualTo("kn");
		assertThat(SIUnits.NEPER).as("Neper symbol is not correct.").isEqualTo("Np");
		assertThat(SIUnits.BEL).as("Bel symbol is not correct.").isEqualTo("B");
		assertThat(SIUnits.DECIBEL).as("Decibel symbol is not correct.").isEqualTo("dB");
	}

	/**
	 * Tests non-SI units associated with the CGS and the CGS-Gaussian system of
	 * units.
	 */
	@Test
	public void testNonSIUnitsCGS() {
		assertThat(SIUnits.ERG).as("Erg symbol is not correct.").isEqualTo("erg");
		assertThat(SIUnits.DYNE).as("Dyne symbol is not correct.").isEqualTo("dyn");
		assertThat(SIUnits.POISE).as("Poise symbol is not correct.").isEqualTo("P");
		assertThat(SIUnits.STOKES).as("Stokes symbol is not correct.").isEqualTo("St");
		assertThat(SIUnits.STILB).as("Stilb symbol is not correct.").isEqualTo("sb");
		assertThat(SIUnits.PHOT).as("Phot symbol is not correct.").isEqualTo("ph");
		assertThat(SIUnits.GAL).as("Gal symbol is not correct.").isEqualTo("Gal");
		assertThat(SIUnits.MAXWELL).as("Maxwell symbol is not correct.").isEqualTo("Mx");
		assertThat(SIUnits.GAUSS).as("Gauss symbol is not correct.").isEqualTo("G");
		assertThat(SIUnits.OERSTED).as("Oersted symbol is not correct.").isEqualTo("Oe");
	}
}
