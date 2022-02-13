package com.jho5245.cucumbery.util.rpg;

public class Level
{
	public static long[] level_exp = new long[250];

	public static long getExp(int level)
	{
		if (level >= 1 && level < 250)
			return level_exp[level - 1];
		return 0;
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 249; i++)
		{
			System.out.println((i + 1) + " -> " + (i + 2) + " : " + level_exp[i]);
		}

		long total = 0;

		for (long xp : level_exp)
		{
			total += xp;
		}

		System.out.println("총 경험치 : " + total);
	}

	static
	{
		level_exp[0] = 15;

		for (int i = 1; i < 10; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.433D);
		}

		for (int i = 10; i < 15; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1]);
		}

		for (int i = 15; i < 30; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.1D);
		}

		for (int i = 30; i < 35; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1]);
		}

		for (int i = 35; i < 60; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.07D);
		}

		for (int i = 60; i < 65; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1]);
		}

		for (int i = 65; i < 100; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.06D);
		}

		for (int i = 100; i < 105; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1]);
		}

		for (int i = 100; i < 140; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.015D);
		}

		for (int i = 140; i < 170; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.01D);
		}

		for (int i = 170; i < 199; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.03D);
		}

		level_exp[199] = Math.round(level_exp[198] * 2.84D);

		for (int i = 200; i < 209; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.2D);
		}

		level_exp[209] = Math.round(level_exp[208] * 2.12D);

		for (int i = 210; i < 219; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.06D);
		}

		level_exp[219] = Math.round(level_exp[218] * 2.08D);

		for (int i = 220; i < 229; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.04D);
		}
		
		level_exp[229] = Math.round(level_exp[228] * 2.04D);

		for (int i = 230; i < 239; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.02D);
		}
		
		level_exp[239] = Math.round(level_exp[238] * 2.02D);
		
		for (int i = 240; i < 249; i++)
		{
			level_exp[i] = Math.round(level_exp[i - 1] * 1.01D);
		}
		// /* */ level_exp[0] = 15;
		// /* */ level_exp[1] = 34;
		// /* */ level_exp[2] = 57;
		// /* */ level_exp[3] = 92;
		// /* */ level_exp[4] = 135;
		// /* */ level_exp[5] = 372;
		// /* */ level_exp[6] = 450;
		// /* */ level_exp[7] = 840;
		// /* */ level_exp[8] = 1242;
		// /* */ level_exp[9] = 1242;
		// /* */ level_exp[10] = 1242;
		// /* */ level_exp[11] = 1242;
		// /* */ level_exp[12] = 1242;
		// /* */ level_exp[13] = 1242;
		// /* */ level_exp[14] = 1490;
		// /* */ level_exp[15] = 1788;
		// /* */ level_exp[16] = 2145;
		// /* */ level_exp[17] = 2574;
		// /* */ level_exp[18] = 3088;
		// /* */ level_exp[19] = 3705;
		// /* */ level_exp[20] = 4446;
		// /* */ level_exp[21] = 5335;
		// /* */ level_exp[22] = 6402;
		// /* */ level_exp[23] = 7682;
		// /* */ level_exp[24] = 9218;
		// /* */ level_exp[25] = 11061;
		// /* */ level_exp[26] = 13273;
		// /* */ level_exp[27] = 15927;
		// /* */ level_exp[28] = 19112;
		// /* */ level_exp[29] = 19112;
		// /* */ level_exp[30] = 19112;
		// /* */ level_exp[31] = 19112;
		// /* */ level_exp[32] = 19112;
		// /* */ level_exp[33] = 19112;
		// /* */ level_exp[34] = 22934;
		// /* */ level_exp[35] = 27520;
		// /* */ level_exp[36] = 33024;
		// /* */ level_exp[37] = 39628;
		// /* */ level_exp[38] = 47553;
		// /* */ level_exp[39] = 51357;
		// /* */ level_exp[40] = 55465;
		// /* */ level_exp[41] = 59902;
		// /* */ level_exp[42] = 64694;
		// /* */ level_exp[43] = 69869;
		// /* */ level_exp[44] = 75458;
		// /* */ level_exp[45] = 81494;
		// /* */ level_exp[46] = 88013;
		// /* */ level_exp[47] = 95054;
		// /* */ level_exp[48] = 102658;
		// /* */ level_exp[49] = 110870;
		// /* */ level_exp[50] = 119739;
		// /* */ level_exp[51] = 129318;
		// /* */ level_exp[52] = 139663;
		// /* */ level_exp[53] = 150836;
		// /* */ level_exp[54] = 162902;
		// /* */ level_exp[55] = 175934;
		// /* */ level_exp[56] = 190008;
		// /* */ level_exp[57] = 205208;
		// /* */ level_exp[58] = 221624;
		// /* */ level_exp[59] = 221624;
		// /* */ level_exp[60] = 221624;
		// /* */ level_exp[61] = 221624;
		// /* */ level_exp[62] = 221624;
		// /* */ level_exp[63] = 221624;
		// /* */ level_exp[64] = 238245;
		// /* */ level_exp[65] = 256113;
		// /* */ level_exp[66] = 275321;
		// /* */ level_exp[67] = 295970;
		// /* */ level_exp[68] = 318167;
		// /* */ level_exp[69] = 342029;
		// /* */ level_exp[70] = 367681;
		// /* */ level_exp[71] = 395257;
		// /* */ level_exp[72] = 424901;
		// /* */ level_exp[73] = 456768;
		// /* */ level_exp[74] = 488741;
		// /* */ level_exp[75] = 522952;
		// /* */ level_exp[76] = 559558;
		// /* */ level_exp[77] = 598727;
		// /* */ level_exp[78] = 640637;
		// /* */ level_exp[79] = 685481;
		// /* */ level_exp[80] = 733464;
		// /* */ level_exp[81] = 784806;
		// /* */ level_exp[82] = 839742;
		// /* */ level_exp[83] = 898523;
		// /* */ level_exp[84] = 961419;
		// /* */ level_exp[85] = 1028718;
		// /* */ level_exp[86] = 1100728;
		// /* */ level_exp[87] = 1177778;
		// /* */ level_exp[88] = 1260222;
		// /* */ level_exp[89] = 1342136;
		// /* */ level_exp[90] = 1429374;
		// /* */ level_exp[91] = 1522283;
		// /* */ level_exp[92] = 1621231;
		// /* */ level_exp[93] = 1726611;
		// /* */ level_exp[94] = 1838840;
		// /* */ level_exp[95] = 1958364;
		// /* */ level_exp[96] = 2085657;
		// /* */ level_exp[97] = 2221224;
		// /* */ level_exp[98] = 2365603;
		// /* */ level_exp[99] = 2365603;
		// /**/ level_exp[100] = 2365603;
		// /**/ level_exp[101] = 2365603;
		// /**/ level_exp[102] = 2365603;
		// /**/ level_exp[103] = 2365603;
		// /**/ level_exp[104] = 2519367;
		// /**/ level_exp[105] = 2683125;
		// /**/ level_exp[106] = 2857528;
		// /**/ level_exp[107] = 3043267;
		// /**/ level_exp[108] = 3241079;
		// /**/ level_exp[109] = 3451749;
		// /**/ level_exp[110] = 3676112;
		// /**/ level_exp[111] = 3915059;
		// /**/ level_exp[112] = 4169537;
		// /**/ level_exp[113] = 4440556;
		// /**/ level_exp[114] = 4729192;
		// /**/ level_exp[115] = 5036589;
		// /**/ level_exp[116] = 5363967;
		// /**/ level_exp[117] = 5712624;
		// /**/ level_exp[118] = 6083944;
		// /**/ level_exp[119] = 6479400;
		// /**/ level_exp[120] = 6900561;
		// /**/ level_exp[121] = 7349097;
		// /**/ level_exp[122] = 7826788;
		// /**/ level_exp[123] = 8335529;
		// /**/ level_exp[124] = 8877338;
		// /**/ level_exp[125] = 9454364;
		// /**/ level_exp[126] = 10068897;
		// /**/ level_exp[127] = 10723375;
		// /**/ level_exp[128] = 11420394;
		// /**/ level_exp[129] = 12162719;
		// /**/ level_exp[130] = 12953295;
		// /**/ level_exp[131] = 13795259;
		// /**/ level_exp[132] = 14691950;
		// /**/ level_exp[133] = 15646926;
		// /**/ level_exp[134] = 16663976;
		// /**/ level_exp[135] = 17747134;
		// /**/ level_exp[136] = 18900697;
		// /**/ level_exp[137] = 20129242;
		// /**/ level_exp[138] = 21437642;
		// /**/ level_exp[139] = 22777494;
		// /**/ level_exp[140] = 24201087;
		// /**/ level_exp[141] = 25713654;
		// /**/ level_exp[142] = 27320757;
		// /**/ level_exp[143] = 29028304;
		// /**/ level_exp[144] = 30842573;
		// /**/ level_exp[145] = 32770233;
		// /**/ level_exp[146] = 34818372;
		// /**/ level_exp[147] = 36994520;
		// /**/ level_exp[148] = 39306677;
		// /**/ level_exp[149] = 41763344;
		// /**/ level_exp[150] = 44373553;
		// /**/ level_exp[151] = 47146900;
		// /**/ level_exp[152] = 50093581;
		// /**/ level_exp[153] = 53224429;
		// /**/ level_exp[154] = 56550955;
		// /**/ level_exp[155] = 60085389;
		// /**/ level_exp[156] = 63840725;
		// /**/ level_exp[157] = 67830770;
		// /**/ level_exp[158] = 72070193;
		// /**/ level_exp[159] = 76574580;
		// /**/ level_exp[160] = 81360491;
		// /**/ level_exp[161] = 86445521;
		// /**/ level_exp[162] = 91848366;
		// /**/ level_exp[163] = 97588888;
		// /**/ level_exp[164] = 103688193;
		// /**/ level_exp[165] = 110168705;
		// /**/ level_exp[166] = 117054249;
		// /**/ level_exp[167] = 124370139;
		// /**/ level_exp[168] = 132143272;
		// /**/ level_exp[169] = 140402226;
		// /**/ level_exp[170] = 149177365;
		// /**/ level_exp[171] = 158500950;
		// /**/ level_exp[172] = 168407259;
		// /**/ level_exp[173] = 178932712;
		// /**/ level_exp[174] = 190116006;
		// /**/ level_exp[175] = 201998256;
		// /**/ level_exp[176] = 214623147;
		// /**/ level_exp[177] = 228037093;
		// /**/ level_exp[178] = 242289411;
		// /**/ level_exp[179] = 256826775;
		// /**/ level_exp[180] = 272236381;
		// /**/ level_exp[181] = 288570563;
		// /**/ level_exp[182] = 305884796;
		// /**/ level_exp[183] = 324237883;
		// /**/ level_exp[184] = 343692155;
		// /**/ level_exp[185] = 364313684;
		// /**/ level_exp[186] = 386172505;
		// /**/ level_exp[187] = 409342855;
		// /**/ level_exp[188] = 433903426;
		// /**/ level_exp[189] = 459937631;
		// /**/ level_exp[190] = 487533888;
		// /**/ level_exp[191] = 516785921;
		// /**/ level_exp[192] = 547793076;
		// /**/ level_exp[193] = 580660660;
		// /**/ level_exp[194] = 615500299;
		// /**/ level_exp[195] = 652430316;
		// /**/ level_exp[196] = 691576134;
		// /**/ level_exp[197] = 733070702;
		// /**/ level_exp[198] = 777054944;
		// /**/ level_exp[199] = 2207026470L;
		// /**/ level_exp[200] = 2648431764L;
		// /**/ level_exp[201] = 3178118116L;
		// /**/ level_exp[202] = 3813741739L;
		// /**/ level_exp[203] = 4576490086L;
		// /**/ level_exp[204] = 5491788103L;
		// /**/ level_exp[205] = 6590145723L;
		// /**/ level_exp[206] = 7908174867L;
		// /**/ level_exp[207] = 9489809840L;
		// /**/ level_exp[208] = 11387771808L;
		// /**/ level_exp[209] = 24142076232L;
		// /**/ level_exp[210] = 25590600805L;
		// /**/ level_exp[211] = 27126036853L;
		// /**/ level_exp[212] = 28753599064L;
		// /**/ level_exp[213] = 30478815007L;
		// /**/ level_exp[214] = 32307543907L;
		// /**/ level_exp[215] = 34245996541L;
		// /**/ level_exp[216] = 36300756333L;
		// /**/ level_exp[217] = 38478801712L;
		// /**/ level_exp[218] = 40787529814L;
		// /**/ level_exp[219] = 84838062013L;
		// /**/ level_exp[220] = 88231584493L;
		// /**/ level_exp[221] = 91760847872L;
		// /**/ level_exp[222] = 95431281786L;
		// /**/ level_exp[223] = 99248533057L;
		// /**/ level_exp[224] = 103218474379L;
		// /**/ level_exp[225] = 107347213354L;
		// /**/ level_exp[226] = 111641101888L;
		// /**/ level_exp[227] = 116106745963L;
		// /**/ level_exp[228] = 120751015801L;
		// /**/ level_exp[229] = 246332072234L;
		// /**/ level_exp[230] = 251258713678L;
		// /**/ level_exp[231] = 256283887951L;
		// /**/ level_exp[232] = 261409565710L;
		// /**/ level_exp[233] = 266637757024L;
		// /**/ level_exp[234] = 271970512164L;
		// /**/ level_exp[235] = 277409922407L;
		// /**/ level_exp[236] = 282958120855L;
		// /**/ level_exp[237] = 288617283272L;
		// /**/ level_exp[238] = 294389628937L;
		// /**/ level_exp[239] = 594667050452L;
		// /**/ level_exp[240] = 600613720956L;
		// /**/ level_exp[241] = 606619858165L;
		// /**/ level_exp[242] = 612686056746L;
		// /**/ level_exp[243] = 618812917313L;
		// /**/ level_exp[244] = 625001046486L;
		// /**/ level_exp[245] = 631251056950L;
		// /**/ level_exp[246] = 637563567519L;
		// /**/ level_exp[247] = 643939203194L;
		// /**/ level_exp[248] = 650378595225L;
	}
}
