package com.sumera.koreactor.util.bundle

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import java.io.Serializable
import java.util.ArrayList

class BundleWrapperImpl(val bundle: Bundle) : BundleWrapper {

    override fun putBoolean(key: String, value: Boolean) {
        bundle.putBoolean(key, value)
    }

    override fun putInt(key: String, value: Int) {
        bundle.putInt(key, value)
    }

    override fun putLong(key: String, value: Long) {
        bundle.putLong(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        bundle.putDouble(key, value)
    }

    override fun putString(key: String, value: String?) {
         bundle.putString(key, value)
    }

    override fun putByte(key: String, value: Byte) {
        bundle.putByte(key, value)
    }

    override fun putChar(key: String, value: Char) {
        bundle.putChar(key, value)
    }

    override fun putShort(key: String, value: Short) {
        bundle.putShort(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        bundle.putFloat(key, value)
    }

    override fun putCharSequence(key: String, value: CharSequence?) {
        bundle.putCharSequence(key, value)
    }

    override fun putParcelable(key: String, value: Parcelable?) {
        bundle.putParcelable(key, value)
    }

    override fun putParcelableArray(key: String, value: Array<Parcelable>?) {
        bundle.putParcelableArray(key, value)
    }

    override fun putParcelableArrayList(key: String, value: ArrayList<out Parcelable>?) {
        bundle.putParcelableArrayList(key, value)
    }

    override fun putSparseParcelableArray(key: String, value: SparseArray<out Parcelable>?) {
        bundle.putSparseParcelableArray(key, value)
    }

    override fun putIntegerArrayList(key: String, value: ArrayList<Int>?) {
        bundle.putIntegerArrayList(key, value)
    }

    override fun putStringArrayList(key: String, value: ArrayList<String>?) {
        bundle.putStringArrayList(key, value)
    }

    override fun putCharSequenceArrayList(key: String, value: ArrayList<CharSequence>?) {
        bundle.putCharSequenceArrayList(key, value)
    }

    override fun putSerializable(key: String, value: Serializable?) {
        bundle.putSerializable(key, value)
    }

    override fun putByteArray(key: String, value: ByteArray?) {
        bundle.putByteArray(key, value)
    }

    override fun putShortArray(key: String, value: ShortArray?) {
        bundle.putShortArray(key, value)
    }

    override fun putCharArray(key: String, value: CharArray?) {
        bundle.putCharArray(key, value)
    }

    override fun putFloatArray(key: String, value: FloatArray?) {
        bundle.putFloatArray(key, value)
    }

    override fun putCharSequenceArray(key: String, value: Array<CharSequence>?) {
        bundle.putCharSequenceArray(key, value)
    }

    override fun putBundle(key: String, value: Bundle?) {
        bundle.putBundle(key, value)
    }

    override fun getBoolean(key: String): Boolean {
        return bundle.getBoolean(key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return bundle.getBoolean(key, defaultValue)
    }

    override fun getInt(key: String): Int {
        return bundle.getInt(key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return bundle.getInt(key, defaultValue)
    }

    override fun getLong(key: String): Long {
        return bundle.getLong(key)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return bundle.getLong(key, defaultValue)
    }

    override fun getDouble(key: String): Double {
        return bundle.getDouble(key)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return bundle.getDouble(key, defaultValue)
    }

    override fun getString(key: String): String? {
        return bundle.getString(key)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return bundle.getString(key, defaultValue)
    }

    override fun getByte(key: String): Byte? {
        return bundle.getByte(key)
    }

    override fun getByte(key: String, defaultValue: Byte): Byte {
        return bundle.getByte(key, defaultValue)
    }

    override fun getChar(key: String): Char {
        return bundle.getChar(key)
    }

    override fun getChar(key: String, defaultValue: Char): Char {
        return bundle.getChar(key, defaultValue)
    }

    override fun getShort(key: String): Short? {
        return bundle.getShort(key)
    }

    override fun getShort(key: String, defaultValue: Short): Short {
        return bundle.getShort(key, defaultValue)
    }

    override fun getFloat(key: String): Float {
        return bundle.getFloat(key)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return bundle.getFloat(key, defaultValue)
    }

    override fun getCharSequence(key: String): CharSequence? {
        return bundle.getCharSequence(key)
    }

    override fun getCharSequence(key: String, defaultValue: CharSequence?): CharSequence? {
        return bundle.getCharSequence(key, defaultValue)
    }

    override fun getBundle(key: String): Bundle? {
        return bundle.getBundle(key)
    }

    override fun <T : Parcelable> getParcelable(key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun getParcelableArray(key: String): Array<Parcelable>? {
        return bundle.getParcelableArray(key)
    }

    override fun <T : Parcelable> getParcelableArrayList(key: String): ArrayList<T>? {
        return bundle.getParcelableArrayList(key)
    }

    override fun <T : Parcelable> getSparseParcelableArray(key: String): SparseArray<T>? {
        return bundle.getSparseParcelableArray(key)
    }

    override fun getSerializable(key: String): Serializable? {
        return bundle.getSerializable(key)
    }

    override fun getIntegerArrayList(key: String): ArrayList<Int>? {
        return bundle.getIntegerArrayList(key)
    }

    override fun getStringArrayList(key: String): ArrayList<String>? {
        return bundle.getStringArrayList(key)
    }

    override fun getCharSequenceArrayList(key: String): ArrayList<CharSequence>? {
        return bundle.getCharSequenceArrayList(key)
    }

    override fun getByteArray(key: String): ByteArray? {
        return bundle.getByteArray(key)
    }

    override fun getShortArray(key: String): ShortArray? {
        return bundle.getShortArray(key)
    }

    override fun getCharArray(key: String): CharArray? {
        return bundle.getCharArray(key)
    }

    override fun getFloatArray(key: String): FloatArray? {
        return bundle.getFloatArray(key)
    }

    override fun getCharSequenceArray(key: String): Array<CharSequence>? {
        return bundle.getCharSequenceArray(key)
    }
}
