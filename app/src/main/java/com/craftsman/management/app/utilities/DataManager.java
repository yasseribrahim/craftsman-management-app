package com.craftsman.management.app.utilities;

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.R;
import com.craftsman.management.app.models.About;
import com.craftsman.management.app.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataManager {
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference NODE_USERS = database.getReference(Constants.NODE_NAME_USERS);

    public static void initUserAdmin() {
        User user = new User();
        user.setId("oehesliVOhWCKZwVRoXqkVV3vX42");
        user.setUsername("admin@craftsman.com");
        user.setPassword("123456");
        user.setType(1);
        NODE_USERS.child("oehesliVOhWCKZwVRoXqkVV3vX42").setValue(user);
    }

    int[] resources = new int[]{
            R.layout.activity_about,
            R.layout.activity_about_edit,
            R.layout.activity_forget_password,
            R.layout.activity_grade_details,
            R.layout.activity_home_admin,
            R.layout.activity_home_client,
            R.layout.activity_home_craftsman,
            R.layout.activity_login,
            R.layout.activity_messaging,
            R.layout.activity_profile,
            R.layout.activity_profile_edit,
            R.layout.activity_registration,
            R.layout.activity_reviews,
            R.layout.activity_service,
            R.layout.activity_service_prices,
            R.layout.activity_splash,
            R.layout.activity_user,
            R.layout.bottom_sheet_review,
            R.layout.content_home,
            R.layout.fragment_bottom_sheet_category_selector,
            R.layout.fragment_bottom_sheet_dialog_price,
            R.layout.fragment_categories,
            R.layout.fragment_contacts_bottom_sheet_dialog,
            R.layout.fragment_craftsmans,
            R.layout.fragment_grades,
            R.layout.fragment_more,
            R.layout.fragment_notifications,
            R.layout.fragment_services,
            R.layout.fragment_services_created,
            R.layout.fragment_users,
            R.layout.item_category,
            R.layout.item_category_selector,
            R.layout.item_chat,
            R.layout.item_chat_other,
            R.layout.item_craftsman,
            R.layout.item_event_viewer,
            R.layout.item_notification,
            R.layout.item_price,
            R.layout.item_review,
            R.layout.item_service,
            R.layout.item_student,
            R.layout.item_user,
            R.layout.layout_app_bar,
            R.layout.layout_loading,
            R.layout.nav_header_main,
    };

    public static void initAbout() {
        About aboutEn = new About();
        About aboutAr = new About();

        aboutEn.setContent("The application displays the university student card electronically and activates the QR code feature, whereby the university can dispense with printed cards and achieve the goals of electronic transformation. And this makes it Easy for extracting the card and not having to print it, as the card is electronic. It is safe to use, as when the plastic card is lost, it can be misused. As for the electronic card, we just change the student’s password.");
        aboutEn.setConditions("The student must have a mobile device and also have the software installed on his device. He must be registered with the university and agree to the terms of use of the application");
        aboutEn.setObjectives("Students and Doctors at the university Lecturers and employees. Retrieval of the students data process becomes easy. The lecturer can manage students in the lecture easily. helping the lecturers take the absence of students more easily. In this way and the process of updating data will be more easily.");
        aboutAr.setContent("يقوم التطبيق بعرض بطاقة الطالب الجامعي إلكترونياً وتفعيل خاصية QR code حيث يمكن للجامعة الاستغناء عن البطاقات المطبوعة وتحقيق أهداف التحول الإلكتروني. وهذا يسهل إخراج البطاقة وعدم الاضطرار إلى طباعتها ، حيث أن البطاقة إلكترونية. إنه آمن للاستخدام ، حيث أنه عند فقدان البطاقة البلاستيكية ، يمكن إساءة استخدامها. بالنسبة للبطاقة الإلكترونية ، نقوم فقط بتغيير كلمة مرور الطالب");
        aboutAr.setConditions("يجب أن يكون لدى الطالب جهاز محمول وأن يكون البرنامج مثبتًا على جهازه أيضًا. يجب أن يكون مسجلاً في الجامعة ويوافق على شروط استخدام التطبيق.");
        aboutAr.setObjectives("الطلاب والأطباء في الجامعة المحاضرون والموظفون. يصبح استرداد عملية بيانات الطالب أمرًا سهلاً. يستطيع المحاضر إدارة الطلاب في المحاضرة بسهولة. مساعدة المحاضرين على استيعاب غياب الطلاب بسهولة أكبر. بهذه الطريقة ستكون عملية تحديث البيانات أكثر سهولة.");

        database.getReference(Constants.NODE_NAME_ABOUT).child("en").setValue(aboutEn);
        database.getReference(Constants.NODE_NAME_ABOUT).child("ar").setValue(aboutAr);
    }
}
