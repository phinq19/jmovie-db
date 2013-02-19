/**
 * 
 */
package com.lars_albrecht.mdb.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lars_albrecht.general.utilities.Helper;

/**
 * @author albrela
 * 
 * @see "http://www.wsoftware.de/practices/proc-execs.html"
 * 
 */
public class ProcExec {

	public static int			SYSTEM_UNKNOWN	= -1;
	public static int			SYSTEM_WIN		= 0;
	public static int			SYSTEM_UNIX		= 1;
	public InputStreamReader	input			= null;

	private static int			system			= ProcExec.SYSTEM_UNKNOWN;

	static boolean isLinuxSystem() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("linux") >= 0;
	}

	static boolean isWindowsSystem() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("windows") >= 0;
	}

	public ProcExec() {
		if (ProcExec.isWindowsSystem()) {
			ProcExec.system = ProcExec.SYSTEM_WIN;
		} else if (ProcExec.isLinuxSystem()) {
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
			// "cmd /c"
			final Process process = Runtime.getRuntime().exec(
					((ProcExec.system == ProcExec.SYSTEM_WIN) ? " " + command : command) + " "
							+ Helper.implode(parameters, " ", null, null));
			this.input = new InputStreamReader(process.getInputStream());
			return new BufferedReader(this.input);
		}
		return null;
	}

}
