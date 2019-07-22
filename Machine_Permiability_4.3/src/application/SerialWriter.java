package application;

import java.io.OutputStream;

public class SerialWriter implements Runnable {
	OutputStream out;
	writeFormat wrt;
	int sleep;

	public SerialWriter(OutputStream out, writeFormat wrt) {
		this.out = out;
		this.wrt = wrt;
		sleep = 0;
	}

	public SerialWriter(OutputStream out, writeFormat wrt, int slp) {
		this.out = out;
		this.wrt = wrt;
		sleep = slp;
	}

	public void run() {
		try {
			Thread.sleep(sleep);
			try {
			for (Integer dout : wrt.wData) {

				// TODO Auto-generated method stub

				
					// Thread.sleep(sleep);
					out.write(dout);
					// System.out.println("NOW SENDING...");
				

			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
