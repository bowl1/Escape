package com.libowen.fakecall.system;

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
public final class FakeCallService_MembersInjector implements MembersInjector<FakeCallService> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  private FakeCallService_MembersInjector(Provider<NotificationHelper> notificationHelperProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(FakeCallService instance) {
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<FakeCallService> create(
      Provider<NotificationHelper> notificationHelperProvider) {
    return new FakeCallService_MembersInjector(notificationHelperProvider);
  }

  @InjectedFieldSignature("com.libowen.fakecall.system.FakeCallService.notificationHelper")
  public static void injectNotificationHelper(FakeCallService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
