package com.sumera.koreactor.testutils

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import com.sumera.koreactor.util.bundle.BundleWrapper
import java.io.Serializable
import java.util.ArrayList

class NoOpBundleWrapper : BundleWrapper {

    override fun putBoolean(key: String, value: Boolean) {
        // NoOp
    }

    override fun putInt(key: String, value: Int) {
        // NoOp
    }

    override fun putLong(key: String, value: Long) {
        // NoOp
    }

    override fun putDouble(key: String, value: Double) {
        // NoOp
    }

    override fun putString(key: String, value: String?) {
        // NoOp
    }

    override fun putByte(key: String, value: Byte) {
        // NoOp
    }

    override fun putChar(key: String, value: Char) {
        // NoOp
    }

    override fun putShort(key: String, value: Short) {
        // NoOp
    }

    override fun putFloat(key: String, value: Float) {
        // NoOp
    }

    override fun putCharSequence(key: String, value: CharSequence?) {
        // NoOp
    }

    override fun putParcelable(key: String, value: Parcelable?) {
        // NoOp
    }

    override fun putParcelableArray(key: String, value: Array<Parcelable>?) {
        // NoOp
    }

    override fun putParcelableArrayList(key: String, value: ArrayList<out Parcelable>?) {
        // NoOp
    }

    override fun putSparseParcelableArray(key: String, value: SparseArray<out Parcelable>?) {
        // NoOp
    }

    override fun putIntegerArrayList(key: String, value: ArrayList<Int>?) {
        // NoOp
    }

    override fun putStringArrayList(key: String, value: ArrayList<String>?) {
        // NoOp
    }

    override fun putCharSequenceArrayList(key: String, value: ArrayList<CharSequence>?) {
        // NoOp
    }

    override fun putSerializable(key: String, value: Serializable?) {
        // NoOp
    }

    override fun putByteArray(key: String, value: ByteArray?) {
        // NoOp
    }

    override fun putShortArray(key: String, value: ShortArray?) {
        // NoOp
    }

    override fun putCharArray(key: String, value: CharArray?) {
        // NoOp
    }

    override fun putFloatArray(key: String, value: FloatArray?) {
        // NoOp
    }

    override fun putCharSequenceArray(key: String, value: Array<CharSequence>?) {
        // NoOp
    }

    override fun putBundle(key: String, value: Bundle?) {
        // NoOp
    }

    override fun getBoolean(key: String): Boolean {
       return false
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return false
    }

    override fun getInt(key: String): Int {
        return 0
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return 0
    }

    override fun getLong(key: String): Long {
        return 0
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return 0
    }

    override fun getDouble(key: String): Double {
        return 0.0
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return 0.0
    }

    override fun getString(key: String): String? {
        return null
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return null
    }

    override fun getByte(key: String): Byte? {
        return 0
    }

    override fun getByte(key: String, defaultValue: Byte): Byte {
        return 0
    }

    override fun getChar(key: String): Char {
        return '0'
    }

    override fun getChar(key: String, defaultValue: Char): Char {
        return '0'
    }

    override fun getShort(key: String): Short? {
        return 0
    }

    override fun getShort(key: String, defaultValue: Short): Short {
        return 0
    }

    override fun getFloat(key: String): Float {
        return 0f
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return 0f
    }

    override fun getCharSequence(key: String): CharSequence? {
        return null
    }

    override fun getCharSequence(key: String, defaultValue: CharSequence?): CharSequence? {
        return null
    }

    override fun getBundle(key: String): Bundle? {
        return null
    }

    override fun <T : Parcelable> getParcelable(key: String): T? {
        return null
    }

    override fun getParcelableArray(key: String): Array<Parcelable>? {
        return null
    }

    override fun <T : Parcelable> getParcelableArrayList(key: String): ArrayList<T>? {
        return null
    }

    override fun <T : Parcelable> getSparseParcelableArray(key: String): SparseArray<T>? {
        return null
    }

    override fun getSerializable(key: String): Serializable? {
        return null
    }

    override fun getIntegerArrayList(key: String): ArrayList<Int>? {
        return null
    }

    override fun getStringArrayList(key: String): ArrayList<String>? {
        return null
    }

    override fun getCharSequenceArrayList(key: String): ArrayList<CharSequence>? {
        return null
    }

    override fun getByteArray(key: String): ByteArray? {
        return null
    }

    override fun getShortArray(key: String): ShortArray? {
        return null
    }

    override fun getCharArray(key: String): CharArray? {
        return null
    }

    override fun getFloatArray(key: String): FloatArray? {
        return null
    }

    override fun getCharSequenceArray(key: String): Array<CharSequence>? {
        return null
    }
}