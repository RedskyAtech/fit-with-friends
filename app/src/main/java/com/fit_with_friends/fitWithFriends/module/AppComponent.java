package com.fit_with_friends.fitWithFriends.module;

import com.fit_with_friends.App;
import com.fit_with_friends.fitWithFriends.ui.activities.challenge.ChallengeDetailsActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.comments.CommentsActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.createChallenge.CreateChallengeActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.forgotPassword.ChangePasswordActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.forgotPassword.ForgotPasswordActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.invite.InviteFriendsActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.notification.NotificationActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.notification.NotificationSettingsActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.password.ChangePasswordSettingActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.policyTypes.PolicyTypeActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.profile.EditProfileActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.search.SearchActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.signIn.SignInActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.signUp.SignUpActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.signUp.SignUpSecondActivity;
import com.fit_with_friends.fitWithFriends.ui.activities.weightLog.WeightLogActivity;
import com.fit_with_friends.fitWithFriends.ui.fragments.competition.CurrentChallengesFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.competition.PastChallengesFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers.ConnectionsFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers.FindChallengersFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers.InvitesFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.home.HomeFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.motivation.TodayMotivationFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.motivation.YesterdayMotivationFragment;
import com.fit_with_friends.fitWithFriends.ui.fragments.settings.SettingsFragment;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DaoModule.class,
                ServiceModule.class,
                MapperModule.class,
                RepositoryModule.class
        }
)

public interface



AppComponent {

    void inject(App app);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(NotificationActivity notificationActivity);

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(SignUpSecondActivity signUpSecondActivity);

    void inject(CreateChallengeActivity createChallengeActivity);

    void inject(HomeFragment homeFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(ChangePasswordSettingActivity changePasswordSettingActivity);

    void inject(ChallengeDetailsActivity challengeDetailsActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(NotificationSettingsActivity notificationSettingsActivity);

    void inject(TodayMotivationFragment todayMotivationFragment);

    void inject(YesterdayMotivationFragment yesterdayMotivationFragment);

    void inject(PolicyTypeActivity policyTypeActivity);

    void inject(ConnectionsFragment connectionsFragment);

    void inject(CurrentChallengesFragment currentChallengesFragment);

    void inject(PastChallengesFragment pastChallengesFragment);

    void inject(InviteFriendsActivity inviteFriendsActivity);

    void inject(InvitesFragment invitesFragment);

    void inject(FindChallengersFragment findChallengersFragment);

    void inject(SearchActivity searchActivity);

    void inject(WeightLogActivity weightLogActivity);

    void inject(CommentsActivity commentsActivity);
}