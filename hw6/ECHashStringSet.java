import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet{
    private static double MIN_LOAD = 0.2;
    private static double MAX_LOAD = 5;

    public ECHashStringSet(int nb) {
        _size = 0;
        _stoe = (LinkedList<String>[]) new LinkedList[nb];
        for (int i = 0; i < nb; i += 1) {
            _stoe[i] = new LinkedList<String>();
        }
    }

    public ECHashStringSet() {
        this((int) (1/MIN_LOAD));
    }

    @Override
    public void put(String s) {
        _size += 1;
        if (s != null) {
            if (_size > _stoe.length * MAX_LOAD) {
                resize();
            }

            _hashcode = hash(s);
            if (!_stoe[_hashcode].contains(s)) {
                _stoe[_hashcode].add(s);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        if (s == null) {
            return  false;
        }
        return _stoe[hash(s)].contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < _stoe.length; i++) {
            if (_stoe[i] != null) {
                for (int j = 0; j < _stoe[i].size(); j++) {
                    result.add(_stoe[i].get(j));
                }
            }
        }
        return result;
    }

    public int size() {
        return _size;
    }

    public void resize() {
        int newBucketCount = _size * 5;
        ECHashStringSet e = new ECHashStringSet(newBucketCount);

        for (int i = 0; i < _stoe.length; i++) {
            for (String s : _stoe[i]) {
                e.put(s);
            }
        }

        _stoe = e._stoe;
    }

    private int hash(String s) {
        return (s.hashCode() & 0x7fffffff) % _stoe.length;
    }

    private int _size;

    private int _hashcode;

    private LinkedList<String>[] _stoe;


}