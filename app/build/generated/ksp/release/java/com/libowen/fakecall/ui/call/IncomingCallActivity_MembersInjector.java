package com.libowen.fakecall.ui.call;

import com.libowen.fakecall.system.NotificationHelper;
import com.libowen.fakecall.system.RingtoneHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class IncomingCallActivity_MembersInjector implements MembersInjector<IncomingCallActivity> {
  private final Provider<RingtoneHelper> ringtoneHelperProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private IncomingCallActivity_MembersInjector(Provider<RingtoneHelper> ringtoneHelperProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.ringtoneHelperProvider = ringtoneHelperProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(IncomingCallActivity instance) {
    injectRingtoneHelper(instance, ringtoneHelperProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<IncomingCallActivity> create(
      Provider<RingtoneHelper> ringtoneHelperProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new IncomingCallActivity_MembersInjector(ringtoneHelperProvider, notificationHelperProvider);
  }

  @InjectedFieldSignature("com.libowen.fakecall.ui.call.IncomingCallActivity.ringtoneHelper")
  public static void injectRingtoneHelper(IncomingCallActivity instance,
      RingtoneHelper ringtoneHelper) {
    instance.ringtoneHelper = ringtoneHelper;
  }

  @InjectedFieldSignature("com.libowen.fakecall.ui.call.IncomingCallActivity.notificationHelper")
  public static void injectNotificationHelper(IncomingCallActivity instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
