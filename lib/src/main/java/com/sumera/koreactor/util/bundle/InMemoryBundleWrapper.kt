package com.sumera.koreactor.util.bundle

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import java.io.Serializable
import java.util.ArrayList

class InMemoryBundleWrapper : BundleWrapper {

    private val map = HashMap<String, Any?>()

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun putInt(key: String, value: Int) {
        map[key] = value
    }

    override fun putLong(key: String, value: Long) {
        map[key] = value
    }

    override fun putDouble(key: String, value: Double) {
        map[key] = value
    }

    override fun putString(key: String, value: String?) {
        map[key] = value
    }

    override fun putByte(key: String, value: Byte) {
        map[key] = value
    }

    override fun putChar(key: String, value: Char) {
        map[key] = value
    }

    override fun putShort(key: String, value: Short) {
        map[key] = value
    }

    override fun putFloat(key: String, value: Float) {
        map[key] = value
    }

    override fun putCharSequence(key: String, value: CharSequence?) {
        map[key] = value
    }

    override fun putParcelable(key: String, value: Parcelable?) {
        map[key] = value
    }

    override fun putParcelableArray(key: String, value: Array<Parcelable>?) {
        map[key] = value
    }

    override fun putParcelableArrayList(key: String, value: ArrayList<out Parcelable>?) {
        map[key] = value
    }

    override fun putSparseParcelableArray(key: String, value: SparseArray<out Parcelable>?) {
        map[key] = value
    }

    override fun putIntegerArrayList(key: String, value: ArrayList<Int>?) {
        map[key] = value
    }

    override fun putStringArrayList(key: String, value: ArrayList<String>?) {
        map[key] = value
    }

    override fun putCharSequenceArrayList(key: String, value: ArrayList<CharSequence>?) {
        map[key] = value
    }

    override fun putSerializable(key: String, value: Serializable?) {
        map[key] = value
    }

    override fun putByteArray(key: String, value: ByteArray?) {
        map[key] = value
    }

    override fun putShortArray(key: String, value: ShortArray?) {
        map[key] = value
    }

    override fun putCharArray(key: String, value: CharArray?) {
        map[key] = value
    }

    override fun putFloatArray(key: String, value: FloatArray?) {
        map[key] = value
    }

    override fun putCharSequenceArray(key: String, value: Array<CharSequence>?) {
        map[key] = value
    }

    override fun putBundle(key: String, value: Bundle?) {
        map[key] = value
    }

    override fun getBoolean(key: String): Boolean {
        return get(key, false)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return get(key, defaultValue)
    }

    override fun getInt(key: String): Int {
        return get(key, 0)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return get(key, defaultValue)
    }

    override fun getLong(key: String): Long {
        return get(key, 0L)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return get(key, defaultValue)
    }

    override fun getDouble(key: String): Double {
        return get(key, 0.0)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return get(key, defaultValue)
    }

    override fun getString(key: String): String? {
        return getNullable<String?>(key, null)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return getNullable<String?>(key, defaultValue)
    }

    override fun getByte(key: String): Byte {
        return get(key, 0)
    }

    override fun getByte(key: String, defaultValue: Byte): Byte {
        return get(key, 0)
    }

    override fun getChar(key: String): Char {
        return get(key, '0')
    }

    override fun getChar(key: String, defaultValue: Char): Char {
        return get(key, defaultValue)
    }

    override fun getShort(key: String): Short {
        return get(key, 0)
    }

    override fun getShort(key: String, defaultValue: Short): Short {
        return get(key, defaultValue)
    }

    override fun getFloat(key: String): Float {
        return get(key, 0f)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return get(key, defaultValue)
    }

    override fun getCharSequence(key: String): CharSequence? {
        return getNullable<CharSequence>(key, null)
    }

    override fun getCharSequence(key: String, defaultValue: CharSequence?): CharSequence? {
        return getNullable(key, defaultValue)
    }

    override fun getBundle(key: String): Bundle? {
        return getNullable<Bundle>(key, null)
    }

    override fun <T : Parcelable> getParcelable(key: String): T? {
        return getNullable<Parcelable>(key, null) as T?
    }

    override fun getParcelableArray(key: String): Array<Parcelable>? {
        return getNullable<Array<Parcelable>>(key, null)
    }

    override fun <T : Parcelable> getParcelableArrayList(key: String): ArrayList<T>? {
        return getNullable<ArrayList<T>>(key, null)
    }

    override fun <T : Parcelable> getSparseParcelableArray(key: String): SparseArray<T>? {
        return getNullable<SparseArray<T>>(key, null)
    }

    override fun getSerializable(key: String): Serializable? {
        return getNullable<Serializable>(key, null)
    }

    override fun getIntegerArrayList(key: String): ArrayList<Int>? {
        return getNullable<ArrayList<Int>>(key, null)
    }

    override fun getStringArrayList(key: String): ArrayList<String>? {
        return getNullable<ArrayList<String>>(key, null)
    }

    override fun getCharSequenceArrayList(key: String): ArrayList<CharSequence>? {
        return getNullable<ArrayList<CharSequence>>(key, null)
    }

    override fun getByteArray(key: String): ByteArray? {
        return getNullable<ByteArray>(key, null)
    }

    override fun getShortArray(key: String): ShortArray? {
        return getNullable<ShortArray>(key, null)
    }

    override fun getCharArray(key: String): CharArray? {
        return getNullable<CharArray>(key, null)
    }

    override fun getFloatArray(key: String): FloatArray? {
        return getNullable<FloatArray>(key, null)
    }

    override fun getCharSequenceArray(key: String): Array<CharSequence>? {
        return getNullable<Array<CharSequence>>(key, null)
    }

    private inline fun <reified T> get(key: String, defaultValue: T): T {
        if (key !in map) {
            return defaultValue
        }
        return map[key] as T
    }

    private inline fun <reified T> getNullable(key: String, defaultValue: T?): T? {
        if (key !in map) {
            return defaultValue
        }
        return map[key] as T?
    }
}