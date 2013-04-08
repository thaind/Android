#include "Box2D.h"
#include "GearJoint.h"

//@line:29

	 JNIEXPORT void JNICALL Java_com_badlogic_gdx_physics_box2d_joints_GearJoint_jniSetRatio(JNIEnv* env, jobject object, jlong addr, jfloat ratio) {


//@line:42

		b2GearJoint* joint =  (b2GearJoint*)addr;
		joint->SetRatio( ratio );
	

}

JNIEXPORT jfloat JNICALL Java_com_badlogic_gdx_physics_box2d_joints_GearJoint_jniGetRatio(JNIEnv* env, jobject object, jlong addr) {


//@line:52

		b2GearJoint* joint =  (b2GearJoint*)addr;
		return joint->GetRatio();
	

}

