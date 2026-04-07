package com.libowen.fakecall.system;

import com.libowen.fakecall.data.datastore.SettingsDataStore;
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
public final class FakeCallReceiver_MembersInjector implements MembersInjector<FakeCallReceiver> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private FakeCallReceiver_MembersInjector(Provider<NotificationHelper> notificationHelperProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public void injectMembers(FakeCallReceiver instance) {
    injectNotificationHelper(instance, notificationHelperProvider.get());
    injectSettingsDataStore(instance, settingsDataStoreProvider.get());
  }

  public static MembersInjector<FakeCallReceiver> create(
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new FakeCallReceiver_MembersInjector(notificationHelperProvider, settingsDataStoreProvider);
  }

  @InjectedFieldSignature("com.libowen.fakecall.system.FakeCallReceiver.notificationHelper")
  public static void injectNotificationHelper(FakeCallReceiver instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }

  @InjectedFieldSignature("com.libowen.fakecall.system.FakeCallReceiver.settingsDataStore")
  public static void injectSettingsDataStore(FakeCallReceiver instance,
      SettingsDataStore settingsDataStore) {
    instance.settingsDataStore = settingsDataStore;
  }
}
