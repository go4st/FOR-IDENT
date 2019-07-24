package de.hswt.fi.msms.service.api;

import de.hswt.fi.msms.service.model.MsMsCandidate;
import de.hswt.fi.msms.service.model.MsMsJob;

import java.util.List;
import java.util.Map;

public interface MsMsService {

	Map<String, List<MsMsCandidate>> process(MsMsJob job);

}
