/**
 * 
 */
package com.lars_albrecht.mdb.main.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lars_albrecht.general.utilities.Helper;

/**
 * @author lalbrecht
 * 
 * @see "http://www.wsoftware.de/practices/proc-execs.html"
 * 
 */
public class ProcExec {

	public static final int		SYSTEM_UNKNOWN	= -1;
	public static final int		SYSTEM_WIN		= 0;
	public static final int		SYSTEM_UNIX		= 1;
	public static final int		SYSTEM_MACOS	= 2;
	public InputStreamReader	input			= null;

	private static int			system			= ProcExec.SYSTEM_UNKNOWN;

	static boolean isLinuxSystem() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("linux") >= 0;
	}

	static boolean isUnixSystem() {
		if (ProcExec.isLinuxSystem()) {
			return true;
		} else {
			final String osName = System.getProperty("os.name").toLowerCase();
			return osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0;
		}
	}

	static boolean isMacOsSystem() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("mac") >= 0;
	}

	static boolean isWindowsSystem() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("windows") >= 0;
	}

	public ProcExec() {
		if (ProcExec.isWindowsSystem()) {
			ProcExec.system = ProcExec.SYSTEM_WIN;
		} else if (ProcExec.isMacOsSystem()) {
			ProcExec.system = ProcExec.SYSTEM_MACOS;
		} else if (ProcExec.isUnixSystem()) {
			ProcExec.system = ProcExec.SYSTEM_UNIX;
		} else {
			ProcExec.system = ProcExec.SYSTEM_UNKNOWN;
		}
	}

	public void closeProc() {
		try {
			if ((this.input != null) && this.input.ready()) {
				this.input.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getProcOutput(final String command, final String[] parameters) throws IOException {
		if (ProcExec.system != ProcExec.SYSTEM_UNKNOWN) {
			String osCommand = null;
			switch (ProcExec.system) {
				case SYSTEM_WIN:
					// "cmd /c"
					osCommand = " " + command + " " + Helper.implode(parameters, " ", null, null);
					break;
				case SYSTEM_MACOS:
					osCommand = "open " + command + " " + Helper.implode(parameters, " ", null, null);
				case SYSTEM_UNIX:
					osCommand = command + " " + Helper.implode(parameters, " ", null, null);
					break;
				default:
					return null;
			}

			final Process process = Runtime.getRuntime().exec(osCommand);
			this.input = new InputStreamReader(process.getInputStream());
			return new BufferedReader(this.input);
		}
		return null;
	}
}
