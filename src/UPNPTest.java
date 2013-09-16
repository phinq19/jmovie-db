import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

/**
 * 
 */

/**
 * @author albrela
 * 
 */
public class UPNPTest {

	public static void main(final String[] args) {
		try {
			new UPNPTest();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public UPNPTest() throws InterruptedException {

		// UPnP discovery is asynchronous, we need a callback
		final RegistryListener listener = new RegistryListener() {

			@Override
			public void remoteDeviceDiscoveryStarted(final Registry registry, final RemoteDevice device) {
				System.out.println("Discovery started: " + device.getDisplayString());
			}

			@Override
			public void remoteDeviceDiscoveryFailed(final Registry registry, final RemoteDevice device, final Exception ex) {
				System.out.println("Discovery failed: " + device.getDisplayString() + " => " + ex);
			}

			@Override
			public void remoteDeviceAdded(final Registry registry, final RemoteDevice device) {
				System.out.println("Remote device available: " + device.getDisplayString());
			}

			@Override
			public void remoteDeviceUpdated(final Registry registry, final RemoteDevice device) {
				System.out.println("Remote device updated: " + device.getDisplayString());
			}

			@Override
			public void remoteDeviceRemoved(final Registry registry, final RemoteDevice device) {
				System.out.println("Remote device removed: " + device.getDisplayString());
			}

			@Override
			public void localDeviceAdded(final Registry registry, final LocalDevice device) {
				System.out.println("Local device added: " + device.getDisplayString());
			}

			@Override
			public void localDeviceRemoved(final Registry registry, final LocalDevice device) {
				System.out.println("Local device removed: " + device.getDisplayString());
			}

			@Override
			public void beforeShutdown(final Registry registry) {
				System.out.println("Before shutdown, the registry has devices: " + registry.getDevices().size());
			}

			@Override
			public void afterShutdown() {
				System.out.println("Shutdown of registry complete!");

			}
		};

		// This will create necessary network resources for UPnP right away
		System.out.println("Starting Cling...");
		final UpnpService upnpService = new UpnpServiceImpl(listener);

		// Send a search message to all devices and services, they should
		// respond soon
		upnpService.getControlPoint().search(new STAllHeader());

		// Let's wait 10 seconds for them to respond
		System.out.println("Waiting 10 seconds before shutting down...");
		Thread.sleep(10000);

		// Release all resources and advertise BYEBYE to other UPnP devices
		System.out.println("Stopping Cling...");
		upnpService.shutdown();

	}

}
