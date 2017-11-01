package ff.projects.service;

import ff.projects.entity.Film;

import java.util.List;

/**
 * Created by F on 2017/6/21.
 */
public interface GatherService {

     Object[] connectFilmForMedia();

     void relation();

     List<Film> listFilmsByPersonId(String personId, String type);


     }
