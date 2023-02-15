# Ichor
Log your heartbeat via your watch!

# Description
You can view your current heart rate, record your heart rate into a local database to be stored on your watch using various sampling speeds, and view your heart rate history. You can also delete your heart rate history if you so choose!

Please note that this app cannot guarantee accuracy, and should not be used inform you on health issues; this app is just for fun.  If you are worried about your health, see a doctor!

## Dependencies
* Android Studio. See also [app level](https://github.com/Carkzis/Ichor/blob/master/app/build.gradle) and [project level](https://github.com/Carkzis/Ichor/blob/master/build.gradle) gradle builds.
* Android SDK 33 for running the app.

## Installing
* You can download the code from the Ichor repository by clicking "Code", then "Download ZIP".
* You can then install this from within Android Studio onto an emulator or a mobile device with a minimum SDK of 30 via the "Run 'app'" command (Shift+F10 by default).

## Executing the program
* You can run the app off a suitable android watch emulator/device.
* Initial Screen: 
	* You will need to provide the app with your permission to take your heart rate, so click the button with the associated icon to the left and grant the app permissions. If you initially refuse this permission, you will need to go into the watch settings to change the it.
	* You can also view the "About" screen from here by clicking the question mark (?).
	
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_permission_awaited.png?raw=true" width="300" />
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_permission_denied.png?raw=true" width="300" />

* Main Screen:
	* You can view your current heart rate on the main screen of the app, it will say, for example "80 bpm", which means 80 beats per minute.
	* The availability of heart rate data will be displayed, which should ideally be "Available" after initially being "Acquiring"; it will otherwise be "Unknown".
	* Your sampling speed dictates how often your current heart rate is added to the heart rate history. It can be "Slow", "Default", or "Fast". Further details are below.
	* Your heart rate history will be displayed at the bottom of the main screen. It shows a list of all recorded heart rates, with their respective heart rate and time of recording. These can be swiped to the edge of the screen if you wish to delete them; more on this below.
	
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_main_screen.png?raw=true" width="300" />
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_heartrate_history.png?raw=true" width="300" />

* Sampling Speed:
	* You can change the sampling rate by clicking the gear button associated with this icon. It will bring up a selection of sampling speed options for you to choose from, with a check mark next to your current option.
	* Slow: This will sample slowly (currently every 20 seconds), and have a delay before initially sampling.
	* Default: This will sample at a moderate pace (currently every 10 seconds), and will initiate sampling immediately.
	* Fast: This will sample often (currently every 5 seconds), and will initiate sampling immediately.
	
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_change_sampling_speed.png?raw=true" width="300" />

* Deleting Records:
	*  You can delete all records by clicking the rubbish bin button and then confirm that you wish to do so via the prompt. Note that this is a nuclear option, and cannot be undone!
	*  You can delete a single record by swiping a record all the way left or right and confirming with the prompt. Note that this cannot be undone!
	
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_delete_all.png?raw=true" width="300" />
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_delete_single_record.png?raw=true" width="300" />

* About Screen:
	* This will provide you will details about the app, much of which can also be seen in this readme!
	
<img src="https://github.com/Carkzis/Ichor/blob/master/ichor_screenshots/ichor_about.png?raw=true" width="300" />
	
* Synthetic Providers:
    * To allow synthetic providers, which can provide fake health information (such as a fake heartbeat) when using an emulator (**which must be in developer mode**), use the following command:
    
        adb shell am broadcast \
        -a "whs.USE_SYNTHETIC_PROVIDERS" \
        com.google.android.wearable.healthservices
    * You can disable them with:
    
        adb shell am broadcast \
        -a "whs.USE_SENSOR_PROVIDERS" \
        com.google.android.wearable.healthservices
    * For walking, try:
    
        adb shell am broadcast \
        -a "whs.synthetic.user.START_WALKING" \
        com.google.android.wearable.healthservices
    * There are many other speeds on https://developer.android.com/training/wearables/health-services/synthetic-data.
    * Stop activity with:
    
        adb shell am broadcast \
        -a "whs.synthetic.user.STOP_EXERCISE" \
        com.google.android.wearable.healthservices
    * You can reset permissions on an emulator using the command "adb shell pm reset-permissions".

## Authors
Marc Jowett (carkzis.apps@gmail.com)

## Version History
* 1.0-SNAPSHOT
  * Pre-release.  See [commits](https://github.com/Carkzis/Ichor/commits/master).

## Acknowledgements
* [The Android Open Source Project](https://source.android.com/) for the fantastic amount of information to help coders in an accessible way.
* [BBC](https://www.bbc.co.uk/), because they are great and have helped me improve my skills as a software engineer.
