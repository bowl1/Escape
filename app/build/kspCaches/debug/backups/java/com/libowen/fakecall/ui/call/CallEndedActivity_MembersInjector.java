package com.libowen.fakecall.ui.call;

import com.libowen.fakecall.system.NotificationHelper;
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
public final class CallEndedActivity_MembersInjector implements MembersInjector<CallEndedActivity> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  private CallEndedActivity_MembersInjector(
      Provider<NotificationHelper> notificationHelperProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(CallEndedActivity instance) {
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<CallEndedActivity> create(
      Provider<NotificationHelper> notificationHelperProvider) {
    return new CallEndedActivity_MembersInjector(notificationHelperProvider);
  }

  @InjectedFieldSignature("com.libowen.fakecall.ui.call.CallEndedActivity.notificationHelper")
  public static void injectNotificationHelper(CallEndedActivity instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
